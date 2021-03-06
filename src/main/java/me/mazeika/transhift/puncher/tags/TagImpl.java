package me.mazeika.transhift.puncher.tags;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Stream;

class TagImpl implements Tag
{
    private final byte[] b;

    @Inject
    public TagImpl(@Assisted final byte[] b)
    {
        if (b.length != LENGTH) {
            throw new IllegalArgumentException("Tag length must be " +
                    LENGTH + ", but got " + b.length);
        }

        this.b = b;
    }

    @Override
    public byte[] get()
    {
        return b.clone();
    }

    @Override
    public boolean equalsArray(final byte[] b)
    {
        return Arrays.equals(this.b, b);
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder(b.length * 3);

        for (byte rawE : b) {
            final int e = rawE < 0 ? rawE & 0xff : rawE;

            builder.append(':');

            if (e < 0x10) {
                builder.append('0');
            }

            builder.append(Integer.toHexString(e));
        }

        return builder.substring(1);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TagImpl oTag = (TagImpl) o;

        return Arrays.equals(b, oTag.b);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(b);
    }
}
