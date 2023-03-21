package com.joel

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows

object TopSpeedWindowing {

  private case class CarEvent(carId: Int, speed: Int, distance: Double, time: Long)

  def main(args: Array[String]): Unit = {
    implicit val env: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.STREAMING)

    val source = KafkaSource.builder()
      .setBootstrapServers("flink-broker:9092")
      .setTopics("cardata")
      .setGroupId("groupid")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build()

    val carData: DataStream[CarEvent] = env
      .fromSource(source, WatermarkStrategy.noWatermarks(), "kafka-source")
      .map(new ParseCarData())
      .name("parse-input")

    val topSpeeds = carData
      .assignAscendingTimestamps(_.time)
      .keyBy(_.carId)
      .window(GlobalWindows.create())
      .maxBy("speed")
      .map(_.toString)

    val sink = KafkaSink.builder()
      .setBootstrapServers("flink-broker:9092")
      .setRecordSerializer(KafkaRecordSerializationSchema.builder()
        .setTopic("topseed")
        .setValueSerializationSchema(new SimpleStringSchema())
        .build()
      )
      .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
      .build();

    topSpeeds
      .sinkTo(sink)
      .name("kafka-sink")

    env.execute()
  }

  private class ParseCarData extends RichMapFunction[String, CarEvent] {
    override def map(in: String): CarEvent = {
      val data = in.split(",")
      CarEvent(data(0).toInt, data(1).toInt, data(2).toDouble, data(3).toLong)
    }
  }

}
