package leejoongseok.wms.common.user;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
public class UserActionRequestWrapper extends HttpServletRequestWrapper { // 상속
    private final Map<String, String[]> params = new ConcurrentHashMap<>();
    private byte[] rawData;

    UserActionRequestWrapper(final HttpServletRequest request) {
        super(request);
        params.putAll(request.getParameterMap()); // 원래의 파라미터를 저장

        try (final InputStream is = request.getInputStream()) {
            rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성

            // body 파싱
            final String collect = getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (!StringUtils.hasText(collect)) { // body 가 없을경우 로깅 제외
                return;
            }
            if (null != request.getContentType() && request.getContentType().contains(
                    MULTIPART_FORM_DATA.getType())) { // 파일 업로드시 로깅제외
                return;
            }
            setParameter("requestBody", collect);
        } catch (final Exception e) {
            log.error("ReadableRequestWrapper init error", e);
        }
    }

    private void setParameter(final String name, final String... values) {
        params.put(name, values);
    }

    @Override
    public String getParameter(final String name) {
        final String[] paramArray = getParameterValues(name);
        return ArrayUtils.isEmpty(paramArray) ? null : paramArray[0];
    }

    @Override
    public String[] getParameterValues(final String name) {
        final String[] dummyParamValue = params.get(name);
        if (null == dummyParamValue) {
            return null;
        }

        final String[] result = new String[dummyParamValue.length];
        System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
        return result;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return 0 == byteArrayInputStream.available();
            }

            @Override
            public boolean isReady() {
                return 0 < byteArrayInputStream.available();
            }

            @Override
            public void setReadListener(final ReadListener readListener) {
                // Do nothing
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}
