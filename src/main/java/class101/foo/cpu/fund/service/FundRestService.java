package class101.foo.cpu.fund.service;

import class101.foo.cpu.fund.dto.FndProdDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class FundRestService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper mapper;

    public FundRestService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Object> sendFundInfo(final FndProdDto fndProdDto) throws Exception {

        Map<String, Object> result = new HashMap<>();
        URL url = new URL("http://localhost:8080/api/v1/funds/sync-fund");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8000));
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

        try (AutoCloseable closeable = () -> {
            log.info("AutoCloseable invoke close()");
            conn.disconnect();
        }) {
            conn.setRequestMethod(HttpMethod.POST.name());
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            Charset charset = Charset.forName("UTF-8");

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), charset));) {
                String body = mapper.writeValueAsString(fndProdDto);
                log.info("request body: [{}]", body);
                bw.write(body);
                bw.flush();
            }

            int responseCode = conn.getResponseCode();
            log.info("status code: [{}]", responseCode);

            // 정상적으로 응답이 오면 형변환
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));) {
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = br.readLine()) != null) {
                        builder.append(str);
                    }

                    try {
                        FndProdDto dto = mapper.readValue(builder.toString().getBytes(), FndProdDto.class);
                        log.info("result: [{}]", dto.toString());
                        result.put("result", dto);
                    } catch (Exception e) {
                        throw new RuntimeException("JSON Parsing Error");
                    }
                }
            } else {
                throw new RuntimeException("통신 실패...");
            }
        }

        return result;
    }
}
