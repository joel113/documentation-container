package com.joel

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.configuration.MemorySize
import org.apache.flink.connector.file.sink.FileSink
import org.apache.flink.connector.file.src.FileSource
import org.apache.flink.connector.file.src.reader.TextLineInputFormat
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows

import java.time.Duration

object TopSpeedWindowing {

  private case class CarEvent(carId: Int, speed: Int, distance: Double, time: Long)

  def main(args: Array[String]): Unit = {
    implicit val env: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.STREAMING)

    val param = ParameterTool.fromArgs(args)
    val inputFolder = param.get("inputFolder")
    val outputFolder = param.get("outputFolder")
    val fileSource = FileSource.forRecordStreamFormat(
      new TextLineInputFormat(),
      new Path(inputFolder))

    val carData: DataStream[CarEvent] = env
      .fromSource(fileSource.build(), WatermarkStrategy.noWatermarks(), "file-input")
      .map(new ParseCarData())
      .name("parse-input")

    val topSpeeds = carData
      .assignAscendingTimestamps(_.time)
      .keyBy(_.carId)
      .window(GlobalWindows.create())
      .maxBy("speed")

    topSpeeds
      .sinkTo(
        FileSink
          .forRowFormat[CarEvent](new Path(outputFolder), new SimpleStringEncoder())
          .withRollingPolicy(
            DefaultRollingPolicy
              .builder()
              .withMaxPartSize(MemorySize.ofMebiBytes(1))
              .withRolloverInterval(Duration.ofSeconds(10))
              .build())
          .build())
      .name("file-sink")
  }

  private class ParseCarData extends RichMapFunction[String, CarEvent] {
    override def map(in: String): CarEvent = {
      val data = in.split(",")
      CarEvent(data(0).toInt, data(1).toInt, data(2).toDouble, data(3).toLong)
    }
  }

}
