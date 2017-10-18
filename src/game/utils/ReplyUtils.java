package game.utils;

/*
 * This class helps to work with replies from server
 */
public class ReplyUtils {
	
	public static boolean isFailed(String reply) {
		return reply.contains("FAILED");
	}
	
	public static String getFailedReason(String reply) {
		return reply.split("FAILED ")[1];
	}
}
