package ru.vitalykhan.voting;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.vitalykhan.voting.controller.json.JsonUtil;
import ru.vitalykhan.voting.model.User;
import ru.vitalykhan.voting.util.exception.ErrorType;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestUtil {
    private TestUtil() {
    }

    public static RequestPostProcessor httpBasicOf(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    private static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static <T> T readFromResultAction(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static ResultMatcher errorTypeIs(ErrorType type) {
        return jsonPath("$.type").value(type.getDescription());
    }

    public static ResultMatcher detailMessageIs(String code) {
        return jsonPath("$.details").value(code);
    }

    public static ResultMatcher parentExceptionIs(String code) {
        return jsonPath("$.parentException").value(code);
    }
}
