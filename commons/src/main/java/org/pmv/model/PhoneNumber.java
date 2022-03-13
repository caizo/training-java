package org.pmv.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Comparator;
import java.util.Formattable;
import java.util.Formatter;



/**
 * Minimize mutability
 * 
 * @author caizo
 *
 */
public class PhoneNumber implements Formattable, Comparable<PhoneNumber> {

	private static final Comparator<PhoneNumber> COMPARATOR = Comparator.
			comparingInt((PhoneNumber p) -> p.areaCode).
			thenComparing(p -> p.number);	
	
	private final int areaCode;
	private final String number;
	private final int hash;
	private final String owner;

	public PhoneNumber(int areaCode, String number, String owner) {
		this.areaCode = areaCode;
		this.number = number;
		this.hash = Objects.hashCode(this.areaCode, this.number);
		this.owner = owner;
	}

	/**
	 * Factory method
	 * 
	 * @param areaCode
	 * @param number
	 * @return
	 */
	public static PhoneNumber of(int areaCode, String number, String owner) {
		if (areaCode > 900) {
			return new InternationalPhoneNumber(areaCode, number, owner);
		}
		return new PhoneNumber(areaCode, number, owner);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof PhoneNumber) {
			PhoneNumber other = (PhoneNumber) obj;
			// next is a problem for performance
//			return Objects.equal(this.areaCode, other.areaCode) && 
//					Objects.equal(this.number, other.number);
			return this.areaCode == other.areaCode && this.number == other.number;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("areaCode", areaCode)
				.add("number", number)
				.add("owner",owner).toString();
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		formatter.format("%d-%d", areaCode, number);
	}

	public int getAreaCode() {
		return areaCode;
	}

	public String getNumber() {
		return number;
	}

	public int getHash() {
		return hash;
	}

	@Override
	public int compareTo(PhoneNumber o) {
		// pre java 8 with guava
//		return ComparisonChain.start().compare(this.areaCode, o.areaCode)
//				.compare(this.number, o.number).result();
		
		// in java 8		
		return COMPARATOR.compare(this, o);
	}

	public String getOwner() {
		return owner;
	}

}
