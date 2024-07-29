package com.github.tagwan.math

import java.util.*

/*
* MTRandom : A Java implementation of the MT19937 (Mersenne Twister)
*            pseudo random number generator algorithm based upon the
*            original C code by Makoto Matsumoto and Takuji Nishimura.
* Author   : David Beaumont
* Email    : mersenne-at-www.goui.net
*
* For the original C code, see:
*     http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html
*
* This version, Copyright (C) 2005, David Beaumont.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

/**
 * @version 1.0
 * @author David Beaumont, Copyright 2005
 *
 *
 * A Java implementation of the MT19937 (Mersenne Twister) pseudo random
 * number generator algorithm based upon the original C code by Makoto
 * Matsumoto and Takuji Nishimura (see [
 * http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html](http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html) for more
 * information.
 *
 *
 * As a subclass of java.util.Random this class provides a single
 * canonical method next() for generating bits in the pseudo random
 * number sequence. Anyone using this class should invoke the public
 * inherited methods (nextInt(), nextFloat etc.) to obtain values as
 * normal. This class should provide a drop-in replacement for the
 * standard implementation of java.util.Random with the additional
 * advantage of having a far longer period and the ability to use a far
 * larger seed value.
 *
 *
 * This is **not** a cryptographically strong source of randomness
 * and should **not** be used for cryptographic systems or in any
 * other situation where true random numbers are required.
 *
 *
 *  [<img alt="CC-GNU LGPL" border="0" src="http://creativecommons.org/images/public/cc-LGPL-a.png"></img>](http://creativecommons.org/licenses/LGPL/2.1/)<br></br>
 * This software is licensed under the [CC-GNU LGPL](http://creativecommons.org/licenses/LGPL/2.1/).
 *
 *
 *
 */
class MTRandom : Random {
    // Internal state
    @Transient
    private var mt: IntArray? = null

    @Transient
    private var mti = 0

    @Transient
    private var compat = false

    // Temporary buffer used during setSeed(long)
    @Transient
    private var ibuf: IntArray? = null

    /**
     * The default constructor for an instance of MTRandom. This invokes the
     * no-argument constructor for java.util.Random which will result in the
     * class being initialised with a seed value obtained by calling
     * System.currentTimeMillis().
     */
    constructor() {}

    /**
     * This version of the constructor can be used to implement identical
     * behaviour to the original C code version of this algorithm including
     * exactly replicating the case where the seed value had not been set prior
     * to calling genrand_int32.
     *
     *
     * If the compatibility flag is set to true, then the algorithm will be
     * seeded with the same default value as was used in the original C code.
     * Furthermore the setSeed() method, which must take a 64 bit long value,
     * will be limited to using only the lower 32 bits of the seed to facilitate
     * seamless migration of existing C code into Java where identical behaviour
     * is required.
     *
     *
     * Whilst useful for ensuring backwards compatibility, it is advised that
     * this feature not be used unless specifically required, due to the
     * reduction in strength of the seed value.
     *
     * @param compatible
     * Compatibility flag for replicating original behaviour.
     */
    constructor(compatible: Boolean) : super(0L) {
        compat = compatible
        setSeed(if (compat) DEFAULT_SEED else System.currentTimeMillis())
    }

    /**
     * This version of the constructor simply initialises the class with the
     * given 64 bit seed value. For a better random number sequence this seed
     * value should contain as much entropy as possible.
     *
     * @param seed
     * The seed value with which to initialise this class.
     */
    constructor(seed: Long) : super(seed) {}

    /**
     * This version of the constructor initialises the class with the given byte
     * array. All the data will be used to initialise this instance.
     *
     * @param buf
     * The non-empty byte array of seed information.
     * @throws NullPointerException
     * if the buffer is null.
     * @throws IllegalArgumentException
     * if the buffer has zero length.
     */
    constructor(buf: ByteArray) : super(0L) {
        setSeed(buf)
    }

    /**
     * This version of the constructor initialises the class with the given
     * integer array. All the data will be used to initialise this instance.
     *
     * @param buf
     * The non-empty integer array of seed information.
     * @throws NullPointerException
     * if the buffer is null.
     * @throws IllegalArgumentException
     * if the buffer has zero length.
     */
    constructor(buf: IntArray) : super(0L) {
        setSeed(buf)
    }

    // Initializes mt[N] with a simple integer seed. This method is
    // required as part of the Mersenne Twister algorithm but need
    // not be made public.
    private fun setSeed(seed: Int) {

        // Annoying runtime check for initialisation of internal data
        // caused by java.util.Random invoking setSeed() during init.
        // This is unavoidable because no fields in our instance will
        // have been initialised at this point, not even if the code
        // were placed at the declaration of the member variable.
        if (mt == null) {
            mt = IntArray(N)
        }

        // ---- Begin Mersenne Twister Algorithm ----
        mt!![0] = seed
        mti = 1
        while (mti < N) {
            mt!![mti] = MAGIC_FACTOR1 * (mt!![mti - 1] xor (mt!![mti - 1] ushr 30)) + mti
            mti++
        }
        // ---- End Mersenne Twister Algorithm ----
    }

    /**
     * This method resets the state of this instance using the 64 bits of seed
     * data provided. Note that if the same seed data is passed to two different
     * instances of MTRandom (both of which share the same compatibility state)
     * then the sequence of numbers generated by both instances will be
     * identical.
     *
     *
     * If this instance was initialised in 'compatibility' mode then this method
     * will only use the lower 32 bits of any seed value passed in and will
     * match the behaviour of the original C code exactly with respect to state
     * initialisation.
     *
     * @param seed
     * The 64 bit value used to initialise the random number
     * generator state.
     */
    @Synchronized
    override fun setSeed(seed: Long) {
        if (compat) {
            setSeed(seed.toInt())
        } else {

            // Annoying runtime check for initialisation of internal data
            // caused by java.util.Random invoking setSeed() during init.
            // This is unavoidable because no fields in our instance will
            // have been initialised at this point, not even if the code
            // were placed at the declaration of the member variable.
            if (ibuf == null) ibuf = IntArray(2)
            ibuf!![0] = seed.toInt()
            ibuf!![1] = (seed ushr 32).toInt()
            setSeed(ibuf!!)
        }
    }

    /**
     * This method resets the state of this instance using the byte array of
     * seed data provided. Note that calling this method is equivalent to
     * calling "setSeed(pack(buf))" and in particular will result in a new
     * integer array being generated during the call. If you wish to retain this
     * seed data to allow the pseudo random sequence to be restarted then it
     * would be more efficient to use the "pack()" method to convert it into an
     * integer array first and then use that to re-seed the instance. The
     * behaviour of the class will be the same in both cases but it will be more
     * efficient.
     *
     * @param buf
     * The non-empty byte array of seed information.
     * @throws NullPointerException
     * if the buffer is null.
     * @throws IllegalArgumentException
     * if the buffer has zero length.
     */
    fun setSeed(buf: ByteArray) {
        setSeed(buf.pack())
    }

    /**
     * This method resets the state of this instance using the integer array of
     * seed data provided. This is the canonical way of resetting the pseudo
     * random number sequence.
     *
     * @param buf
     * The non-empty integer array of seed information.
     * @throws NullPointerException
     * if the buffer is null.
     * @throws IllegalArgumentException
     * if the buffer has zero length.
     */
    @Synchronized
    fun setSeed(buf: IntArray) {
        val length = buf.size
        require(length != 0) { "Seed buffer may not be empty" }
        // ---- Begin Mersenne Twister Algorithm ----
        var i = 1
        var j = 0
        var k = if (N > length) N else length
        setSeed(MAGIC_SEED)
        while (k > 0) {
            mt!![i] = mt!![i] xor (mt!![i - 1] xor (mt!![i - 1] ushr 30)) * MAGIC_FACTOR2 + buf[j] + j
            i++
            j++
            if (i >= N) {
                mt!![0] = mt!![N - 1]
                i = 1
            }
            if (j >= length) j = 0
            k--
        }
        k = N - 1
        while (k > 0) {
            mt!![i] = mt!![i] xor (mt!![i - 1] xor (mt!![i - 1] ushr 30)) * MAGIC_FACTOR3 - i
            i++
            if (i >= N) {
                mt!![0] = mt!![N - 1]
                i = 1
            }
            k--
        }
        mt!![0] = UPPER_MASK // MSB is 1; assuring non-zero initial array
        // ---- End Mersenne Twister Algorithm ----
    }

    /**
     * This method forms the basis for generating a pseudo random number
     * sequence from this class. If given a value of 32, this method behaves
     * identically to the genrand_int32 function in the original C code and
     * ensures that using the standard nextInt() function (inherited from
     * Random) we are able to replicate behaviour exactly.
     *
     *
     * Note that where the number of bits requested is not equal to 32 then bits
     * will simply be masked out from the top of the returned integer value.
     * That is to say that:
     *
     * <pre>
     * mt.setSeed(12345);
     * int foo = mt.nextInt(16) + (mt.nextInt(16) &lt;&lt; 16);
    </pre> *
     *
     * will not give the same result as
     *
     * <pre>
     * mt.setSeed(12345);
     * int foo = mt.nextInt(32);
    </pre> *
     *
     * @param bits
     * The number of significant bits desired in the output.
     * @return The next value in the pseudo random sequence with the specified
     * number of bits in the lower part of the integer.
     */
    @Synchronized
    override fun next(bits: Int): Int {
        // ---- Begin Mersenne Twister Algorithm ----
        var y: Int
        var kk: Int
        if (mti >= N) { // generate N words at one time

            // In the original C implementation, mti is checked here
            // to determine if initialisation has occurred; if not
            // it initialises this instance with DEFAULT_SEED (5489).
            // This is no longer necessary as initialisation of the
            // Java instance must result in initialisation occurring
            // Use the constructor MTRandom(true) to enable backwards
            // compatible behaviour.
            kk = 0
            while (kk < N - M) {
                y = mt!![kk] and UPPER_MASK or (mt!![kk + 1] and LOWER_MASK)
                mt!![kk] = mt!![kk + M] xor (y ushr 1) xor MAGIC[y and 0x1]
                kk++
            }
            while (kk < N - 1) {
                y = mt!![kk] and UPPER_MASK or (mt!![kk + 1] and LOWER_MASK)
                mt!![kk] = mt!![kk + (M - N)] xor (y ushr 1) xor MAGIC[y and 0x1]
                kk++
            }
            y = mt!![N - 1] and UPPER_MASK or (mt!![0] and LOWER_MASK)
            mt!![N - 1] = mt!![M - 1] xor (y ushr 1) xor MAGIC[y and 0x1]
            mti = 0
        }
        y = mt!![mti++]

        // Tempering
        y = y xor (y ushr 11)
        y = y xor (y shl 7 and MAGIC_MASK1)
        y = y xor (y shl 15 and MAGIC_MASK2)
        y = y xor (y ushr 18)
        // ---- End Mersenne Twister Algorithm ----
        return y ushr 32 - bits
    }

    companion object {
        /**
         * Auto-generated serial version UID. Note that MTRandom does NOT support
         * serialisation of its internal state and it may even be necessary to
         * implement read/write methods to re-seed it properly. This is only here to
         * make Eclipse shut up about it being missing.
         */
        private const val serialVersionUID = -515082678588212038L
    }
}

// Constants used in the original C implementation
private const val UPPER_MASK = -0x80000000
private const val LOWER_MASK = 0x7fffffff
private const val N = 624
private const val M = 397
private val MAGIC = intArrayOf(0x0, -0x66f74f21)
private const val MAGIC_FACTOR1 = 1812433253
private const val MAGIC_FACTOR2 = 1664525
private const val MAGIC_FACTOR3 = 1566083941
private const val MAGIC_MASK1 = -0x62d3a980
private const val MAGIC_MASK2 = -0x103a0000
private const val MAGIC_SEED = 19650218
private const val DEFAULT_SEED = 5489L

// This is a fairly obscure little code section to pack a
// byte[] into an int[] in little endian ordering.
/**
 * This simply utility method can be used in cases where a byte array of
 * seed data is to be used to repeatedly re-seed the random number sequence.
 * By packing the byte array into an integer array first, using this method,
 * and then invoking setSeed() with that; it removes the need to re-pack the
 * byte array each time setSeed() is called.
 *
 *
 * If the length of the byte array is not a multiple of 4 then it is
 * implicitly padded with zeros as necessary. For example:
 *
 * <pre>
 * byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 }
</pre> *
 *
 * becomes
 *
 * <pre>
 * int[]  { 0x04030201, 0x00000605 }
</pre> *
 *
 *
 * Note that this method will not complain if the given byte array is empty
 * and will produce an empty integer array, but the setSeed() method will
 * throw an exception if the empty integer array is passed to it.
 *
 * @param buf
 * The non-null byte array to be packed.
 * @return A non-null integer array of the packed bytes.
 * @throws NullPointerException
 * if the given byte array is null.
 */
fun ByteArray.pack(): IntArray {
    val buf = this@pack
    var k: Int
    val blen = buf.size
    val ilen = buf.size + 3 ushr 2
    val ibuf = IntArray(ilen)
    for (n in 0 until ilen) {
        var m = n + 1 shl 2
        if (m > blen) m = blen
        k = buf[--m].toInt() and 0xff
        while (m and 0x3 != 0) {
            k = k shl 8 or (buf[--m].toInt() and 0xff)
        }
        ibuf[n] = k
    }
    return ibuf
}