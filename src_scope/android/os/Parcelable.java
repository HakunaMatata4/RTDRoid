
package android.os;

public interface Parcelable {
    /**
     * Flag for use with {@link #writeToParcel}: the object being written is a
     * return value, that is the result of a function such as "
     * <code>Parcelable someFunction()</code>", "
     * <code>void someFunction(out Parcelable)</code>", or "
     * <code>void someFunction(inout Parcelable)</code>". Some implementations
     * may want to release resources at this point.
     */
    public static final int PARCELABLE_WRITE_RETURN_VALUE = 0x0001;

    /**
     * Bit masks for use with {@link #describeContents}: each bit represents a
     * kind of object considered to have potential special significance when
     * marshalled.
     */
    public static final int CONTENTS_FILE_DESCRIPTOR = 0x0001;

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     * 
     * @return a bitmask indicating the set of special object types marshalled
     *         by the Parcelable.
     */
    public int describeContents();

    /**
     * Flatten this object in to a Parcel.
     * 
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May
     *            be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    public void writeToParcel(Parcel dest, int flags);

    /**
     * Interface that must be implemented and provided as a public CREATOR field
     * that generates instances of your Parcelable class from a Parcel.
     */
    public interface Creator<T> {
        /**
         * Create a new instance of the Parcelable class, instantiating it from
         * the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         * 
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        public T createFromParcel(Parcel source);

        /**
         * Create a new array of the Parcelable class.
         * 
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         *         initialized to null.
         */
        public T[] newArray(int size);
    }
}
