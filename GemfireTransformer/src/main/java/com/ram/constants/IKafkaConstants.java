package com.ram.constants;

// constants for kafka consumer
public interface IKafkaConstants {
	public static String KAFKA_BROKER = "localhost:9092";
	
	public static Integer NO_FAILED_POLL_COUNT=1000;
	
	public static String CLIENT_ID="3";
	
	public static String TOPIC_NAME="EnrichmentTransformer";
	
	public static String GROUP_ID_CONFIG="consumerGroup10";
	
	public static Integer MAX_NO_POSTED_JSON=50;
	

	
	public static String OFFSET_RESET_EARLIER="earliest";
	
	public static Integer MAX_POLL_RECORDS=1;
}
