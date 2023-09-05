package io.confluent.examples.streams.algebird

import org.apache.kafka.streams.processor.StateStoreContext
import org.apache.kafka.streams.processor.internals.{ProcessorStateManager, RecordCollector}
import org.apache.kafka.streams.state.StateSerdes

/**
  * Copied from Kafka's [[org.apache.kafka.streams.processor.internals.ProcessorContextImpl]].
  *
  * If StoreChangeLogger had been public, we would have used it as-is.
  *
  * Note that the use of array-typed keys is discouraged because they result in incorrect caching
  * behavior.  If you intend to work on byte arrays as key, for example, you may want to wrap them
  * with the [[org.apache.kafka.common.utils.Bytes]] class.
  */
class CMSStoreChangeLogger[K, V](val storeName: String,
                                 val context: StateStoreContext,
                                 val partition: Int,
                                 val serialization: StateSerdes[K, V]) {

  private val topic = ProcessorStateManager.storeChangelogTopic(context.applicationId, storeName, context.taskId().topologyName())
  private val collector = context.asInstanceOf[RecordCollector.Supplier].recordCollector

  def this(storeName: String, context: StateStoreContext, serialization: StateSerdes[K, V]) = {
    this(storeName, context, context.taskId.partition(), serialization)
  }

  def logChange(key: K, value: V, timestamp: Long): Unit = {
    if (collector != null) {
      val keySerializer = serialization.keySerializer
      val valueSerializer = serialization.valueSerializer
      collector.send(this.topic, key, value, null, Integer.valueOf(1), java.lang.Long.valueOf(2), keySerializer, valueSerializer, null, null)
    }
  }

}
