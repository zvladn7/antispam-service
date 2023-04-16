package ru.spbstu.kafka.publisher;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.spbstu.antispam.Activity;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.util.DateUtil;

import java.util.Collections;
import java.util.List;

public class ActivityInfoMapperTest {

    @Test
    public void convertNullInputListTest() {
        List<ActivityInfoDTO> activityInfoDTOS = ActivityInfoMapper.convert(null);
        Assert.assertNotNull(activityInfoDTOS);
        Assert.assertEquals(0, activityInfoDTOS.size());
    }

    @Test
    public void convertEmptyInputListTest() {
        List<ActivityInfoDTO> activityInfoDTOS = ActivityInfoMapper.convert(Collections.emptyList());
        Assert.assertNotNull(activityInfoDTOS);
        Assert.assertEquals(0, activityInfoDTOS.size());
    }

    @Test
    public void convertNotEmptyInputListTest() {
        List<ActivityInfo> activityInfos = ImmutableList.of(
                new ActivityInfo(Activity.IP_FIRST_TIME, DateUtil.currentDateCompact() - 60 * 60),
                new ActivityInfo(Activity.IP_LAST_TIME, DateUtil.currentDateCompact())
        );
        List<ActivityInfoDTO> expectedActivityInfoDTOS = ImmutableList.of(
                new ActivityInfoDTO(Activity.IP_FIRST_TIME.getName(), DateUtil.currentDateCompact() - 60 * 60),
                new ActivityInfoDTO(Activity.IP_LAST_TIME.getName(), DateUtil.currentDateCompact())
        );
        List<ActivityInfoDTO> activityInfoDTOS = ActivityInfoMapper.convert(activityInfos);
        assertActivityDTOListsEquals(expectedActivityInfoDTOS, activityInfoDTOS);
    }

    @Test
    public void convertToStringNullInputListTest() {
        String result = ActivityInfoMapper.convertToString(null);
        Assert.assertNotNull(result);
        Assert.assertEquals("null", result);
    }

    @Test
    public void convertToStringEmptyInputListTest() {
        String result = ActivityInfoMapper.convertToString(Collections.emptyList());
        Assert.assertNotNull(result);
        Assert.assertEquals("null", result);
    }

    @Test
    public void convertToStringNotEmptyInputListTest() {
        int firstTime = DateUtil.currentDateCompact() - 60 * 60;
        int lastTime = DateUtil.currentDateCompact();
        String expectedResult = Activity.IP_FIRST_TIME.getName() + "=" + firstTime + "\n"
                + Activity.IP_LAST_TIME.getName() + "=" + lastTime + "\n";
        List<ActivityInfo> activityInfos = ImmutableList.of(
                new ActivityInfo(Activity.IP_FIRST_TIME, firstTime),
                new ActivityInfo(Activity.IP_LAST_TIME, lastTime)
        );
        String result = ActivityInfoMapper.convertToString(activityInfos);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void convertFromStringNullInputTest() {
        List<ActivityInfo> activityInfos = ActivityInfoMapper.convertFromString(null);
        Assert.assertNotNull(activityInfos);
        Assert.assertEquals(0, activityInfos.size());
    }

    @Test
    public void convertFromStringNullStringInputTest() {
        List<ActivityInfo> activityInfos = ActivityInfoMapper.convertFromString("null");
        Assert.assertNotNull(activityInfos);
        Assert.assertEquals(0, activityInfos.size());
    }

    @Test
    public void convertFromStringNotNullInputTest() {
        int firstTime = DateUtil.currentDateCompact() - 60 * 60;
        int lastTime = DateUtil.currentDateCompact();
        String inputString = Activity.IP_FIRST_TIME.getName() + "=" + firstTime + "\n"
                + Activity.IP_LAST_TIME.getName() + "=" + lastTime + "\n";
        List<ActivityInfo> expectedActivityInfos = ImmutableList.of(
                new ActivityInfo(Activity.IP_FIRST_TIME, firstTime),
                new ActivityInfo(Activity.IP_LAST_TIME, lastTime)
        );
        List<ActivityInfo> activityInfos = ActivityInfoMapper.convertFromString(inputString);
        assertActivityInfoListsEquals(expectedActivityInfos, activityInfos);
    }

    private static void assertActivityDTOListsEquals(@Nullable List<ActivityInfoDTO> expectedList,
                                                     @Nullable List<ActivityInfoDTO> actualList) {
        if (expectedList == null && actualList == null) {
            return;
        }
        if (expectedList == null || actualList == null) {
            throw new IllegalStateException("one of passed parameters is null, another is not");
        }
        if (expectedList.size() != actualList.size()) {
            throw new IllegalStateException("Expected size is " + expectedList.size() + ", actual size is " + actualList.size());
        }
        for (int i = 0; i < expectedList.size(); ++i) {
            assertActivityDTOEquals(expectedList.get(i), actualList.get(i));
        }
    }

    private static void assertActivityDTOEquals(@Nullable ActivityInfoDTO expected,
                                                @Nullable ActivityInfoDTO actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            throw new IllegalStateException("one of passed parameters is null, another is not");
        }
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getValue(), actual.getValue());
    }

    private static void assertActivityInfoListsEquals(@Nullable List<ActivityInfo> expectedList,
                                                      @Nullable List<ActivityInfo> actualList) {
        if (expectedList == null && actualList == null) {
            return;
        }
        if (expectedList == null || actualList == null) {
            throw new IllegalStateException("one of passed parameters is null, another is not");
        }
        if (expectedList.size() != actualList.size()) {
            throw new IllegalStateException("Expected size is " + expectedList.size() + ", actual size is " + actualList.size());
        }
        for (int i = 0; i < expectedList.size(); ++i) {
            assertActivityEquals(expectedList.get(i), actualList.get(i));
        }
    }

    private static void assertActivityEquals(@Nullable ActivityInfo expected,
                                             @Nullable ActivityInfo actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            throw new IllegalStateException("one of passed parameters is null, another is not");
        }
        if (expected.getActivity() == null && actual.getActivity() == null) {
            return;
        }
        if (expected.getActivity() == null || actual.getActivity() == null) {
            throw new IllegalStateException("one of activity is null, another is not");
        }
        Assert.assertEquals(expected.getActivity().getId(), actual.getActivity().getId());
        Assert.assertEquals(expected.getActivity().getName(), actual.getActivity().getName());
        Assert.assertEquals(expected.getValue(), actual.getValue());
    }

}
