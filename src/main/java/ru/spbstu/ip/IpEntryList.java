package ru.spbstu.ip;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IpEntryList {

    private final List<IpEntry> ipEntries;

    public IpEntryList(@NotNull List<IpEntry> ipEntries) {
        Validate.notNull(ipEntries);
        this.ipEntries = ipEntries;
    }

    public List<IpEntry> getIpEntries() {
        return ipEntries;
    }

}
