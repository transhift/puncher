package me.mazeika.transhift.puncher.options;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;

class OptionsImpl implements Options
{
    private final String host;
    private final int port;

    @Inject
    public OptionsImpl(@Assisted final String host, @Assisted final int port)
    {
        this.host = host;
        this.port = port;
    }

    @Override
    public String host()
    {
        return host;
    }

    @Override
    public int port()
    {
        return port;
    }
}
