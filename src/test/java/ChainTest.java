import com.google.common.collect.Lists;
import common.CheckResultCode;
import core.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule.RuleStrategy;
import rule.TrdConsumer;
import rule.TrdFunction;
import service.map.MapDataWrapper;
import service.map.MapRuleContext;
import service.map.chain.MapPropertiesCheckChain;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class ChainTest {

    private static Supplier<Map<String, Object>> mapSupplier = () -> {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Ko");
        map.put("age", new Random().nextInt(100));
        return map;
    };

    @Test
    public void testDefaultPropertiesChain() {
        DefaultPropertiesCheckChain defaultPropertiesCheckChain = new DefaultPropertiesCheckChain();
        defaultPropertiesCheckChain.getPredicateChain(new ChainContext(RuleStrategy.FULL_CHECK));
        defaultPropertiesCheckChain.getFunction(new ChainContext(RuleStrategy.FAIL_FAST));
    }

    @Test
    public void testChainContext() {
        ChainContext chainContext = new ChainContext(RuleStrategy.FAIL_FAST);
        chainContext.setRuleStrategy(RuleStrategy.FAIL_FAST);
        Assertions.assertEquals(RuleStrategy.FAIL_FAST, chainContext.getRuleStrategy());
    }

    @Test
    public void testChainContextCache() {
        ChainContext chainContext = new ChainContext();
        Optional<Object> empty = chainContext.getCache("empty");
        Assertions.assertFalse(empty.isPresent());

        chainContext.setCache("key", "val");
        Optional<Object> key = chainContext.getCache("key");
        Assertions.assertTrue(key.isPresent());
        Assertions.assertTrue(StringUtils.equals("val", key.get().toString()));
    }

    @Test
    public void testChainContextInheritableCache() {
        ChainContext chainContext = new ChainContext();
        Optional<Object> empty = chainContext.getInheritableCache("empty");
        Assertions.assertFalse(empty.isPresent());

        chainContext.setInheritableCache("key", "val");
        new Thread(() -> {
            Optional<Object> key = chainContext.getInheritableCache("key");
            Assertions.assertTrue(key.isPresent());
            Assertions.assertTrue(StringUtils.equals("val", key.get().toString()));
        }).start();
        new Thread(() -> {
            Optional<Object> key = chainContext.getInheritableCache("key");
            Assertions.assertTrue(key.isPresent());
            Assertions.assertTrue(StringUtils.equals("val", key.get().toString()));
        }).start();
    }

    @Test
    public void testIChain() {
        class TestChain implements IChain<String, String> {
        }
        TestChain testChain = new TestChain();
        ChainContext chainContext = new ChainContext();
        Function<String, String> testChainFunction = testChain.getFunction(chainContext);
        Predicate<String> testChainPredicateChain = testChain.getPredicateChain(chainContext);
        Assertions.assertEquals("Test", testChainFunction.apply("Test"));
        Assertions.assertTrue(testChainPredicateChain.test("Test"));
    }

    @Test
    public void testTrdConsumer() {
        TrdConsumer<String, String, String> consumer1 = (s1, s2, s3) -> {
        };
        TrdConsumer<String, String, String> consumer2 = (s1, s2, s3) -> {
        };

        consumer1.andThen(consumer2).accept("1", "2", "3");

    }

    @Test
    public void testTrdFunction() {
        TrdFunction<String, String, String, String> function1 = (s1, s2, s3) -> s1;
        Function<String, String> function2 = s1 -> s1;
        String result = function1.andThen(function2).apply("1", "2", "3");
        Assertions.assertTrue(StringUtils.equals("1", result));

    }


    @Test
    public void testChainBuilderOnTest() {

        int size = 6;

        List<Map<String, Object>> data = Lists.newArrayListWithCapacity(size);
        for (int i = 0; i < size; i++) {
            data.add(mapSupplier.get());

        }

        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(size);
        for (Map<String, Object> map : data) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        for (MapDataWrapper m : mapDataWrappers) {
            chain.test(m);
        }

        Assertions.assertTrue(mapDataWrappers
                .parallelStream()
                .allMatch(mapDataWrapper -> mapDataWrapper
                        .getRuleContext()
                        .getResult()
                        .get("name")
                        .isEmpty()));
    }

    @Test
    public void testExecutorOnSize96() {
        // less than 100
        int size = 6;

        List<Map<String, Object>> data = Lists.newArrayListWithCapacity(size);
        for (int i = 0; i < size; i++) {
            data.add(mapSupplier.get());

        }

        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(size);
        for (Map<String, Object> map : data) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        chain.test(mapDataWrappers);
        chain.apply(mapDataWrappers);

        Assertions.assertTrue(mapDataWrappers
                .stream()
                .allMatch(mapDataWrapper -> mapDataWrapper
                        .getRuleContext()
                        .getResult()
                        .toString()
                        .contains(CheckResultCode.DUPLICATED.getReason())));
    }

    @Test
    public void testExecutorOnSize1000() {
        // more than 100 and size%6 != 0
        int size = 1000;

        List<Map<String, Object>> data = Lists.newArrayListWithCapacity(size);
        for (int i = 0; i < size; i++) {
            data.add(mapSupplier.get());

        }

        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(size);
        for (Map<String, Object> map : data) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        chain.test(mapDataWrappers);
        chain.apply(mapDataWrappers);

        Assertions.assertTrue(mapDataWrappers
                .stream()
                .allMatch(mapDataWrapper -> mapDataWrapper
                        .getRuleContext()
                        .getResult()
                        .toString()
                        .contains(CheckResultCode.DUPLICATED.getReason())));

    }

    @Test
    public void testExecutorOnSize1600() {

        // more than 100 and size%6 = 0
        int size = 1600;

        List<Map<String, Object>> data = Lists.newArrayListWithCapacity(size);
        for (int i = 0; i < size; i++) {
            data.add(mapSupplier.get());

        }

        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(size);
        for (Map<String, Object> map : data) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        chain.test(mapDataWrappers);
        chain.apply(mapDataWrappers);

        Assertions.assertTrue(mapDataWrappers
                .stream()
                .allMatch(mapDataWrapper -> mapDataWrapper
                        .getRuleContext()
                        .getResult()
                        .toString()
                        .contains(CheckResultCode.DUPLICATED.getReason())));
    }
}
