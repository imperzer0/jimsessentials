package com.github.jimsessentials.lib.interfaces;

import static com.github.jimsessentials.JimsEssentials.Log;

public abstract class Module
{
    public final String MODULE_ID;

    protected Module(String module_id)
    {
        MODULE_ID = module_id;

        String name = this.getClass().getCanonicalName();
        if (name.isEmpty()) name = this.getClass().getName();
        Log.debug("Loading {} module...", name);
    }
}
