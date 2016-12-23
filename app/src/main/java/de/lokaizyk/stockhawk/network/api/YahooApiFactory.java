package de.lokaizyk.stockhawk.network.api;

import de.lokaizyk.stockhawk.BuildConfig;
import de.lokaizyk.stockhawk.network.ServiceFactory;

/**
 * Created by lars on 22.10.16.
 */

public class YahooApiFactory extends ServiceFactory<YahooApi> {
    public YahooApiFactory() {
        super(YahooApi.class, BuildConfig.BASE_URL);
    }
}
