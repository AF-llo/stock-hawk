package de.lokaizyk.stockhawk.network.api;

import de.lokaizyk.stockhawk.network.model.MultiQueryResponse;
import de.lokaizyk.stockhawk.network.model.SingleQueryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lars on 22.10.16.
 */

public interface YahooApi {

    String METHOD_YQL = "yql";

    String PARAM_Q = "q";

    String VALUE_QUERY_SYMBOL = "select * from yahoo.finance.quotes where symbol in (";

    String PARAM_FORMAT = "format";

    String VALUE_FORMAT_JSON = "json";

    String PARAM_DIAGNOSTICS = "diagnostics";

    String VALUE_DIAGNOSTICS_TRUE = "true";

    String VALUE_DIAGNOSTICS_FALSE = "false";

    String PARAM_ENV = "env";

    String VALUE_ENV_STORE = "store://datatables.org/alltableswithkeys";

    String PARAM_CALLBACK = "callback";

    String VALUE_CALLBACK_EMPTY = " ";

    String INITIAL_SYMBOL_AAPL = "AAPL";

    String INITIAL_SYMBOL_GOOG = "GOOG";

    String INITIAL_SYMBOL_MSFT = "MSFT";

    String INITIAL_SYMBOL_YHOO = "YHOO";

    String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    // https://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.quotes where symbol in ("AAPL","GOOG","MSFT","YHOO")&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=

    @GET(METHOD_YQL)
    Call<MultiQueryResponse> loadStocks(@Query(value = PARAM_Q) String query);

    @GET(METHOD_YQL)
    Call<SingleQueryResponse> loadStock(@Query(value = PARAM_Q) String query);

    class SymbolBuilder {

        private int count = 0;

        private boolean hasSymbols = false;

        private StringBuffer mSymbolBuffer;

        public SymbolBuilder() {
            mSymbolBuffer = new StringBuffer(VALUE_QUERY_SYMBOL);
        }

        public SymbolBuilder addInitialSymbols() {
            addSymbol(INITIAL_SYMBOL_AAPL);
            addSymbol(INITIAL_SYMBOL_GOOG);
            addSymbol(INITIAL_SYMBOL_MSFT);
            addSymbol(INITIAL_SYMBOL_YHOO);
            return this;
        }

        public SymbolBuilder addSymbol(String symbol) {
            hasSymbols = true;
            mSymbolBuffer.append("\"")
                    .append(symbol)
                    .append("\"")
                    .append(",");
            count++;
            return this;
        }

        public int getSymbolCount() {
            return count;
        }

        public String buildSymbolQueryValue() {
            if (hasSymbols) {
                mSymbolBuffer.replace(mSymbolBuffer.length() - 1, mSymbolBuffer.length(), ")");
            }
            return mSymbolBuffer.toString();
        }
    }

}
