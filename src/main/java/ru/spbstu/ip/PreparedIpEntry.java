package ru.spbstu.ip;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PreparedIpEntry {

    private final IpEntry ipEntry;
    private final GeoIpFeatures geoIpFeatures;
    private List<FeatureKeyValue> featureKeyValues;

    public PreparedIpEntry(@NotNull IpEntry ipEntry,
                           @NotNull GeoIpFeatures geoIpFeatures) {
        Validate.notNull(ipEntry);
        Validate.notNull(geoIpFeatures);
        this.ipEntry = ipEntry;
        this.geoIpFeatures = geoIpFeatures;
    }

    public IpEntry getIpEntry() {
        return ipEntry;
    }

    public long getCoordinates() {
        return geoIpFeatures.getCoordinates();
    }

    public List<FeatureKeyValue> getFeatureKeyValues() {
        return featureKeyValues;
    }

    public void setFeatureKeyValues(List<FeatureKeyValue> featureKeyValues) {
        this.featureKeyValues = featureKeyValues;
    }

}