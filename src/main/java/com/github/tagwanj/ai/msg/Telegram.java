
package com.github.tagwanj.ai.msg;

import com.github.tagwanj.internal.IMemoryObject;

/**电报
 * <br>
 * A Telegram is the container of a message. The {@link MessageDispatcher} manages telegram life-cycle.
 * @author davebaol */
public class Telegram implements Comparable<Telegram>, IMemoryObject {

	/** Indicates that the sender doesn't need any return receipt */
	public static final int RETURN_RECEIPT_UNNEEDED = 0;

	/** Indicates that the sender needs the return receipt */
	public static final int RETURN_RECEIPT_NEEDED = 1;

	/** Indicates that the return receipt has been sent back to the original sender of the telegram */
	public static final int RETURN_RECEIPT_SENT = 2;

	/** The agent that sent this telegram */
	public Telegraph sender;

	/** The agent that is to receive this telegram */
	public Telegraph receiver;

	/** The message type. */
	public int message;

	/** The return receipt status of this telegram. Its value should be {@link #RETURN_RECEIPT_UNNEEDED}, {@link #RETURN_RECEIPT_NEEDED} or
	 * {@link #RETURN_RECEIPT_SENT}. */
	public int returnReceiptStatus;

	/** Messages can be dispatched immediately or delayed for a specified amount of time. If a delay is necessary, this field is
	 * stamped with the time the message should be dispatched. */
	private long timestamp;

	/** Any additional information that may accompany the message */
	public Object extraInfo;

	/** Creates an empty {@code Telegram}. */
	public Telegram () {
	}

	/** Returns the time stamp of this telegram. */
	public long getTimestamp () {
		return timestamp;
	}

	/** Sets the time stamp of this telegram. */
	public void setTimestamp (long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void reset () {
		this.sender = null;
		this.receiver = null;
		this.message = 0;
		this.returnReceiptStatus = RETURN_RECEIPT_UNNEEDED;
		this.extraInfo = null;
		this.timestamp = 0;
	}

	@Override
	public int compareTo (Telegram other) {
		if (this.equals(other)) return 0;
		return (this.timestamp - other.timestamp < 0) ? -1 : 1;
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + message;
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + Float.floatToIntBits(timestamp);
		return result;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Telegram other = (Telegram)obj;
		if (message != other.message) return false;
		if (Float.floatToIntBits(timestamp) != Float.floatToIntBits(other.timestamp)) return false;
		if (sender == null) {
			if (other.sender != null) return false;
		} else if (!sender.equals(other.sender)) return false;
		if (receiver == null) {
			if (other.receiver != null) return false;
		} else if (!receiver.equals(other.receiver)) return false;
		return true;
	}

}
