package com.tony.billing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangwenjie 2020/7/13
 */
@Slf4j
@RunWith(JUnit4.class)
public class UnitTest {

    @Test
    public void testJson() {
        String resultStr = "/** * 测试数据 * @type {arry} *//*2020-07-13 13:09:48*//*单位净值走势 equityReturn-净值回报 unitMoney-每份派送金*/var Data_netWorthTrend = [{\"x\":1513872000000,\"y\":1.0,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1514390400000,\"y\":1.0005,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1514476800000,\"y\":1.0009,\"equityReturn\":0.04,\"unitMoney\":\"\"},{\"x\":1514649600000,\"y\":1.0015,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1514822400000,\"y\":1.0076,\"equityReturn\":0.6694,\"unitMoney\":\"\"},{\"x\":1514908800000,\"y\":1.0095,\"equityReturn\":0.1886,\"unitMoney\":\"\"},{\"x\":1514995200000,\"y\":1.0133,\"equityReturn\":0.3764,\"unitMoney\":\"\"},{\"x\":1515081600000,\"y\":1.0143,\"equityReturn\":0.0987,\"unitMoney\":\"\"},{\"x\":1515340800000,\"y\":1.0197,\"equityReturn\":0.5324,\"unitMoney\":\"\"},{\"x\":1515427200000,\"y\":1.0146,\"equityReturn\":-0.5001,\"unitMoney\":\"\"},{\"x\":1515513600000,\"y\":1.0185,\"equityReturn\":0.3844,\"unitMoney\":\"\"},{\"x\":1515600000000,\"y\":1.015,\"equityReturn\":-0.3436,\"unitMoney\":\"\"},{\"x\":1515686400000,\"y\":1.016,\"equityReturn\":0.0985,\"unitMoney\":\"\"},{\"x\":1515945600000,\"y\":1.0117,\"equityReturn\":-0.4232,\"unitMoney\":\"\"},{\"x\":1516032000000,\"y\":1.0198,\"equityReturn\":0.8006,\"unitMoney\":\"\"},{\"x\":1516118400000,\"y\":1.021,\"equityReturn\":0.1177,\"unitMoney\":\"\"},{\"x\":1516204800000,\"y\":1.031,\"equityReturn\":0.9794,\"unitMoney\":\"\"},{\"x\":1516291200000,\"y\":1.0418,\"equityReturn\":1.0475,\"unitMoney\":\"\"},{\"x\":1516550400000,\"y\":1.0541,\"equityReturn\":1.1806,\"unitMoney\":\"\"},{\"x\":1516636800000,\"y\":1.0731,\"equityReturn\":1.8025,\"unitMoney\":\"\"},{\"x\":1516723200000,\"y\":1.0871,\"equityReturn\":1.3046,\"unitMoney\":\"\"},{\"x\":1516809600000,\"y\":1.0937,\"equityReturn\":0.6071,\"unitMoney\":\"\"},{\"x\":1516896000000,\"y\":1.0954,\"equityReturn\":0.1554,\"unitMoney\":\"\"},{\"x\":1517155200000,\"y\":1.0845,\"equityReturn\":-0.9951,\"unitMoney\":\"\"},{\"x\":1517241600000,\"y\":1.0613,\"equityReturn\":-2.1392,\"unitMoney\":\"\"},{\"x\":1517328000000,\"y\":1.0706,\"equityReturn\":0.8763,\"unitMoney\":\"\"},{\"x\":1517414400000,\"y\":1.0655,\"equityReturn\":-0.4764,\"unitMoney\":\"\"},{\"x\":1517500800000,\"y\":1.0878,\"equityReturn\":2.0929,\"unitMoney\":\"\"},{\"x\":1517760000000,\"y\":1.1173,\"equityReturn\":2.7119,\"unitMoney\":\"\"},{\"x\":1517846400000,\"y\":1.0859,\"equityReturn\":-2.8103,\"unitMoney\":\"\"},{\"x\":1517932800000,\"y\":1.0613,\"equityReturn\":-2.2654,\"unitMoney\":\"\"},{\"x\":1518019200000,\"y\":1.0336,\"equityReturn\":-2.61,\"unitMoney\":\"\"},{\"x\":1518105600000,\"y\":0.9902,\"equityReturn\":-4.1989,\"unitMoney\":\"\"},{\"x\":1518364800000,\"y\":1.0017,\"equityReturn\":1.1614,\"unitMoney\":\"\"},{\"x\":1518451200000,\"y\":1.011,\"equityReturn\":0.9284,\"unitMoney\":\"\"},{\"x\":1518537600000,\"y\":1.02,\"equityReturn\":0.8902,\"unitMoney\":\"\"},{\"x\":1519228800000,\"y\":1.035,\"equityReturn\":1.4706,\"unitMoney\":\"\"},{\"x\":1519315200000,\"y\":1.0394,\"equityReturn\":0.4251,\"unitMoney\":\"\"},{\"x\":1519574400000,\"y\":1.0537,\"equityReturn\":1.3758,\"unitMoney\":\"\"},{\"x\":1519660800000,\"y\":1.0336,\"equityReturn\":-1.9076,\"unitMoney\":\"\"},{\"x\":1519747200000,\"y\":1.031,\"equityReturn\":-0.2515,\"unitMoney\":\"\"},{\"x\":1519833600000,\"y\":1.0306,\"equityReturn\":-0.0388,\"unitMoney\":\"\"},{\"x\":1519920000000,\"y\":1.0164,\"equityReturn\":-1.3778,\"unitMoney\":\"\"},{\"x\":1520179200000,\"y\":1.0072,\"equityReturn\":-0.9052,\"unitMoney\":\"\"},{\"x\":1520265600000,\"y\":1.0227,\"equityReturn\":1.5389,\"unitMoney\":\"\"},{\"x\":1520352000000,\"y\":1.0121,\"equityReturn\":-1.0365,\"unitMoney\":\"\"},{\"x\":1520438400000,\"y\":1.014,\"equityReturn\":0.1877,\"unitMoney\":\"\"},{\"x\":1520524800000,\"y\":1.0094,\"equityReturn\":-0.4536,\"unitMoney\":\"\"},{\"x\":1520784000000,\"y\":1.0173,\"equityReturn\":0.7826,\"unitMoney\":\"\"},{\"x\":1520870400000,\"y\":1.0128,\"equityReturn\":-0.4423,\"unitMoney\":\"\"},{\"x\":1520956800000,\"y\":1.0119,\"equityReturn\":-0.0889,\"unitMoney\":\"\"},{\"x\":1521043200000,\"y\":1.0095,\"equityReturn\":-0.2372,\"unitMoney\":\"\"},{\"x\":1521129600000,\"y\":1.0032,\"equityReturn\":-0.6241,\"unitMoney\":\"\"},{\"x\":1521388800000,\"y\":0.9942,\"equityReturn\":-0.8971,\"unitMoney\":\"\"},{\"x\":1521475200000,\"y\":0.9972,\"equityReturn\":0.3018,\"unitMoney\":\"\"},{\"x\":1521561600000,\"y\":0.991,\"equityReturn\":-0.6217,\"unitMoney\":\"\"},{\"x\":1521648000000,\"y\":0.9832,\"equityReturn\":-0.7871,\"unitMoney\":\"\"},{\"x\":1521734400000,\"y\":0.9512,\"equityReturn\":-3.2547,\"unitMoney\":\"\"},{\"x\":1521993600000,\"y\":0.9492,\"equityReturn\":-0.2103,\"unitMoney\":\"\"},{\"x\":1522080000000,\"y\":0.9548,\"equityReturn\":0.59,\"unitMoney\":\"\"},{\"x\":1522166400000,\"y\":0.9468,\"equityReturn\":-0.8379,\"unitMoney\":\"\"},{\"x\":1522252800000,\"y\":0.9642,\"equityReturn\":1.8378,\"unitMoney\":\"\"},{\"x\":1522339200000,\"y\":0.9617,\"equityReturn\":-0.2593,\"unitMoney\":\"\"},{\"x\":1522598400000,\"y\":0.9659,\"equityReturn\":0.4367,\"unitMoney\":\"\"},{\"x\":1522684800000,\"y\":0.9576,\"equityReturn\":-0.8593,\"unitMoney\":\"\"},{\"x\":1522771200000,\"y\":0.9572,\"equityReturn\":-0.0418,\"unitMoney\":\"\"},{\"x\":1523203200000,\"y\":0.9655,\"equityReturn\":0.8671,\"unitMoney\":\"\"},{\"x\":1523289600000,\"y\":0.9879,\"equityReturn\":2.32,\"unitMoney\":\"\"},{\"x\":1523376000000,\"y\":0.9953,\"equityReturn\":0.7491,\"unitMoney\":\"\"},{\"x\":1523462400000,\"y\":0.9862,\"equityReturn\":-0.9143,\"unitMoney\":\"\"},{\"x\":1523548800000,\"y\":0.9807,\"equityReturn\":-0.5577,\"unitMoney\":\"\"},{\"x\":1523808000000,\"y\":0.959,\"equityReturn\":-2.2127,\"unitMoney\":\"\"},{\"x\":1523894400000,\"y\":0.9552,\"equityReturn\":-0.3962,\"unitMoney\":\"\"},{\"x\":1523980800000,\"y\":0.9711,\"equityReturn\":1.6646,\"unitMoney\":\"\"},{\"x\":1524067200000,\"y\":0.9818,\"equityReturn\":1.1018,\"unitMoney\":\"\"},{\"x\":1524153600000,\"y\":0.9674,\"equityReturn\":-1.4667,\"unitMoney\":\"\"},{\"x\":1524412800000,\"y\":0.9633,\"equityReturn\":-0.4238,\"unitMoney\":\"\"},{\"x\":1524499200000,\"y\":0.9865,\"equityReturn\":2.4084,\"unitMoney\":\"\"},{\"x\":1524585600000,\"y\":0.9807,\"equityReturn\":-0.5879,\"unitMoney\":\"\"},{\"x\":1524672000000,\"y\":0.9692,\"equityReturn\":-1.1726,\"unitMoney\":\"\"},{\"x\":1524758400000,\"y\":0.9756,\"equityReturn\":0.6603,\"unitMoney\":\"\"},{\"x\":1525190400000,\"y\":0.9739,\"equityReturn\":-0.1743,\"unitMoney\":\"\"},{\"x\":1525276800000,\"y\":0.9776,\"equityReturn\":0.3799,\"unitMoney\":\"\"},{\"x\":1525363200000,\"y\":0.9724,\"equityReturn\":-0.5319,\"unitMoney\":\"\"},{\"x\":1525622400000,\"y\":0.9833,\"equityReturn\":1.1209,\"unitMoney\":\"\"},{\"x\":1525708800000,\"y\":0.9903,\"equityReturn\":0.7119,\"unitMoney\":\"\"},{\"x\":1525795200000,\"y\":0.9841,\"equityReturn\":-0.6261,\"unitMoney\":\"\"},{\"x\":1525881600000,\"y\":0.9835,\"equityReturn\":-0.061,\"unitMoney\":\"\"},{\"x\":1525968000000,\"y\":0.9845,\"equityReturn\":0.1017,\"unitMoney\":\"\"},{\"x\":1526227200000,\"y\":0.9826,\"equityReturn\":-0.193,\"unitMoney\":\"\"},{\"x\":1526313600000,\"y\":0.9819,\"equityReturn\":-0.0712,\"unitMoney\":\"\"},{\"x\":1526400000000,\"y\":0.9722,\"equityReturn\":-0.9879,\"unitMoney\":\"\"},{\"x\":1526486400000,\"y\":0.9711,\"equityReturn\":-0.1131,\"unitMoney\":\"\"},{\"x\":1526572800000,\"y\":0.9768,\"equityReturn\":0.587,\"unitMoney\":\"\"},{\"x\":1526832000000,\"y\":0.9779,\"equityReturn\":0.1126,\"unitMoney\":\"\"},{\"x\":1526918400000,\"y\":0.9715,\"equityReturn\":-0.6545,\"unitMoney\":\"\"},{\"x\":1527004800000,\"y\":0.95009999999999994,\"equityReturn\":-2.2028,\"unitMoney\":\"\"},{\"x\":1527091200000,\"y\":0.9478,\"equityReturn\":-0.2421,\"unitMoney\":\"\"},{\"x\":1527177600000,\"y\":0.9399,\"equityReturn\":-0.8335,\"unitMoney\":\"\"},{\"x\":1527436800000,\"y\":0.9368,\"equityReturn\":-0.3298,\"unitMoney\":\"\"},{\"x\":1527523200000,\"y\":0.9344,\"equityReturn\":-0.2562,\"unitMoney\":\"\"},{\"x\":1527609600000,\"y\":0.9168,\"equityReturn\":-1.8836,\"unitMoney\":\"\"},{\"x\":1527696000000,\"y\":0.9278,\"equityReturn\":1.1998,\"unitMoney\":\"\"},{\"x\":1527782400000,\"y\":0.9311,\"equityReturn\":0.3557,\"unitMoney\":\"\"},{\"x\":1528041600000,\"y\":0.9353,\"equityReturn\":0.4511,\"unitMoney\":\"\"},{\"x\":1528128000000,\"y\":0.9372,\"equityReturn\":0.2031,\"unitMoney\":\"\"},{\"x\":1528214400000,\"y\":0.936,\"equityReturn\":-0.128,\"unitMoney\":\"\"},{\"x\":1528300800000,\"y\":0.9467,\"equityReturn\":1.1432,\"unitMoney\":\"\"},{\"x\":1528387200000,\"y\":0.9375,\"equityReturn\":-0.9718,\"unitMoney\":\"\"},{\"x\":1528646400000,\"y\":0.94,\"equityReturn\":0.2667,\"unitMoney\":\"\"},{\"x\":1528732800000,\"y\":0.9491,\"equityReturn\":0.9681,\"unitMoney\":\"\"},{\"x\":1528819200000,\"y\":0.9435,\"equityReturn\":-0.59,\"unitMoney\":\"\"},{\"x\":1528905600000,\"y\":0.9484,\"equityReturn\":0.5193,\"unitMoney\":\"\"},{\"x\":1528992000000,\"y\":0.9458,\"equityReturn\":-0.2741,\"unitMoney\":\"\"},{\"x\":1529337600000,\"y\":0.916,\"equityReturn\":-3.1508,\"unitMoney\":\"\"},{\"x\":1529424000000,\"y\":0.9235,\"equityReturn\":0.8188,\"unitMoney\":\"\"},{\"x\":1529510400000,\"y\":0.9206,\"equityReturn\":-0.314,\"unitMoney\":\"\"},{\"x\":1529596800000,\"y\":0.9317,\"equityReturn\":1.2057,\"unitMoney\":\"\"},{\"x\":1529856000000,\"y\":0.9221,\"equityReturn\":-1.0304,\"unitMoney\":\"\"},{\"x\":1529942400000,\"y\":0.9056,\"equityReturn\":-1.7894,\"unitMoney\":\"\"},{\"x\":1530028800000,\"y\":0.8949,\"equityReturn\":-1.1815,\"unitMoney\":\"\"},{\"x\":1530115200000,\"y\":0.8913,\"equityReturn\":-0.4023,\"unitMoney\":\"\"},{\"x\":1530201600000,\"y\":0.9077,\"equityReturn\":1.84,\"unitMoney\":\"\"},{\"x\":1530288000000,\"y\":0.9076,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1530460800000,\"y\":0.8888,\"equityReturn\":-2.0822,\"unitMoney\":\"\"},{\"x\":1530547200000,\"y\":0.8894,\"equityReturn\":0.0675,\"unitMoney\":\"\"},{\"x\":1530633600000,\"y\":0.8878,\"equityReturn\":-0.1799,\"unitMoney\":\"\"},{\"x\":1530720000000,\"y\":0.8819,\"equityReturn\":-0.6646,\"unitMoney\":\"\"},{\"x\":1530806400000,\"y\":0.8861,\"equityReturn\":0.4762,\"unitMoney\":\"\"},{\"x\":1531065600000,\"y\":0.9051,\"equityReturn\":2.1442,\"unitMoney\":\"\"},{\"x\":1531152000000,\"y\":0.9144,\"equityReturn\":1.0275,\"unitMoney\":\"\"},{\"x\":1531238400000,\"y\":0.9116,\"equityReturn\":-0.3062,\"unitMoney\":\"\"},{\"x\":1531324800000,\"y\":0.9198,\"equityReturn\":0.8995,\"unitMoney\":\"\"},{\"x\":1531411200000,\"y\":0.9206,\"equityReturn\":0.087,\"unitMoney\":\"\"},{\"x\":1531670400000,\"y\":0.916,\"equityReturn\":-0.4997,\"unitMoney\":\"\"},{\"x\":1531756800000,\"y\":0.9082,\"equityReturn\":-0.8515,\"unitMoney\":\"\"},{\"x\":1531843200000,\"y\":0.9058,\"equityReturn\":-0.2643,\"unitMoney\":\"\"},{\"x\":1531929600000,\"y\":0.9142,\"equityReturn\":0.9274,\"unitMoney\":\"\"},{\"x\":1532016000000,\"y\":0.9332,\"equityReturn\":2.0783,\"unitMoney\":\"\"},{\"x\":1532275200000,\"y\":0.9493,\"equityReturn\":1.7252,\"unitMoney\":\"\"},{\"x\":1532361600000,\"y\":0.9609,\"equityReturn\":1.222,\"unitMoney\":\"\"},{\"x\":1532448000000,\"y\":0.9614,\"equityReturn\":0.052,\"unitMoney\":\"\"},{\"x\":1532534400000,\"y\":0.9509,\"equityReturn\":-1.0922,\"unitMoney\":\"\"},{\"x\":1532620800000,\"y\":0.9551,\"equityReturn\":0.4417,\"unitMoney\":\"\"},{\"x\":1532880000000,\"y\":0.9597,\"equityReturn\":0.4816,\"unitMoney\":\"\"},{\"x\":1532966400000,\"y\":0.9613,\"equityReturn\":0.1667,\"unitMoney\":\"\"},{\"x\":1533052800000,\"y\":0.9448,\"equityReturn\":-1.7164,\"unitMoney\":\"\"},{\"x\":1533139200000,\"y\":0.9452,\"equityReturn\":0.0423,\"unitMoney\":\"\"},{\"x\":1533225600000,\"y\":0.9441,\"equityReturn\":-0.1164,\"unitMoney\":\"\"},{\"x\":1533484800000,\"y\":0.9411,\"equityReturn\":-0.3178,\"unitMoney\":\"\"},{\"x\":1533571200000,\"y\":0.9636,\"equityReturn\":2.3908,\"unitMoney\":\"\"},{\"x\":1533657600000,\"y\":0.9586,\"equityReturn\":-0.5189,\"unitMoney\":\"\"},{\"x\":1533744000000,\"y\":0.9642,\"equityReturn\":0.5842,\"unitMoney\":\"\"},{\"x\":1533830400000,\"y\":0.9602,\"equityReturn\":-0.4149,\"unitMoney\":\"\"},{\"x\":1534089600000,\"y\":0.964,\"equityReturn\":0.3958,\"unitMoney\":\"\"},{\"x\":1534176000000,\"y\":0.9571,\"equityReturn\":-0.7158,\"unitMoney\":\"\"},{\"x\":1534262400000,\"y\":0.9485,\"equityReturn\":-0.8985,\"unitMoney\":\"\"},{\"x\":1534348800000,\"y\":0.9448,\"equityReturn\":-0.3901,\"unitMoney\":\"\"},{\"x\":1534435200000,\"y\":0.9406,\"equityReturn\":-0.4445,\"unitMoney\":\"\"},{\"x\":1534694400000,\"y\":0.9466,\"equityReturn\":0.6379,\"unitMoney\":\"\"},{\"x\":1534780800000,\"y\":0.952,\"equityReturn\":0.5705,\"unitMoney\":\"\"},{\"x\":1534867200000,\"y\":0.9495,\"equityReturn\":-0.2626,\"unitMoney\":\"\"},{\"x\":1534953600000,\"y\":0.9477,\"equityReturn\":-0.1896,\"unitMoney\":\"\"},{\"x\":1535040000000,\"y\":0.948,\"equityReturn\":0.0317,\"unitMoney\":\"\"},{\"x\":1535299200000,\"y\":0.9556,\"equityReturn\":0.8017,\"unitMoney\":\"\"},{\"x\":1535385600000,\"y\":0.9587,\"equityReturn\":0.3244,\"unitMoney\":\"\"},{\"x\":1535472000000,\"y\":0.9539,\"equityReturn\":-0.5007,\"unitMoney\":\"\"},{\"x\":1535558400000,\"y\":0.941,\"equityReturn\":-1.3523,\"unitMoney\":\"\"},{\"x\":1535644800000,\"y\":0.9372,\"equityReturn\":-0.4038,\"unitMoney\":\"\"},{\"x\":1535904000000,\"y\":0.934,\"equityReturn\":-0.3414,\"unitMoney\":\"\"},{\"x\":1535990400000,\"y\":0.9376,\"equityReturn\":0.3854,\"unitMoney\":\"\"},{\"x\":1536076800000,\"y\":0.9269,\"equityReturn\":-1.1412,\"unitMoney\":\"\"},{\"x\":1536163200000,\"y\":0.9264,\"equityReturn\":-0.0539,\"unitMoney\":\"\"},{\"x\":1536249600000,\"y\":0.9311,\"equityReturn\":0.5073,\"unitMoney\":\"\"},{\"x\":1536508800000,\"y\":0.9243,\"equityReturn\":-0.7303,\"unitMoney\":\"\"},{\"x\":1536595200000,\"y\":0.9085,\"equityReturn\":-1.7094,\"unitMoney\":\"\"},{\"x\":1536681600000,\"y\":0.9058,\"equityReturn\":-0.2972,\"unitMoney\":\"\"},{\"x\":1536768000000,\"y\":0.9084,\"equityReturn\":0.287,\"unitMoney\":\"\"},{\"x\":1536854400000,\"y\":0.9102,\"equityReturn\":0.1982,\"unitMoney\":\"\"},{\"x\":1537113600000,\"y\":0.9035,\"equityReturn\":-0.7361,\"unitMoney\":\"\"},{\"x\":1537200000000,\"y\":0.9149,\"equityReturn\":1.2618,\"unitMoney\":\"\"},{\"x\":1537286400000,\"y\":0.923,\"equityReturn\":0.8853,\"unitMoney\":\"\"},{\"x\":1537372800000,\"y\":0.9234,\"equityReturn\":0.0433,\"unitMoney\":\"\"},{\"x\":1537459200000,\"y\":0.9351,\"equityReturn\":1.2671,\"unitMoney\":\"\"},{\"x\":1537804800000,\"y\":0.9303,\"equityReturn\":-0.5133,\"unitMoney\":\"\"},{\"x\":1537891200000,\"y\":0.9345,\"equityReturn\":0.4515,\"unitMoney\":\"\"},{\"x\":1537977600000,\"y\":0.9311,\"equityReturn\":-0.3638,\"unitMoney\":\"\"},{\"x\":1538064000000,\"y\":0.9355,\"equityReturn\":0.4726,\"unitMoney\":\"\"},{\"x\":1538928000000,\"y\":0.9214,\"equityReturn\":-1.5072,\"unitMoney\":\"\"},{\"x\":1539014400000,\"y\":0.9231,\"equityReturn\":0.1845,\"unitMoney\":\"\"},{\"x\":1539100800000,\"y\":0.9304,\"equityReturn\":0.7908,\"unitMoney\":\"\"},{\"x\":1539187200000,\"y\":0.9101,\"equityReturn\":-2.1819,\"unitMoney\":\"\"},{\"x\":1539273600000,\"y\":0.9202,\"equityReturn\":1.1098,\"unitMoney\":\"\"},{\"x\":1539532800000,\"y\":0.9126,\"equityReturn\":-0.8259,\"unitMoney\":\"\"},{\"x\":1539619200000,\"y\":0.8999,\"equityReturn\":-1.3916,\"unitMoney\":\"\"},{\"x\":1539705600000,\"y\":0.9076,\"equityReturn\":0.8557,\"unitMoney\":\"\"},{\"x\":1539792000000,\"y\":0.8862,\"equityReturn\":-2.3579,\"unitMoney\":\"\"},{\"x\":1539878400000,\"y\":0.8943,\"equityReturn\":0.914,\"unitMoney\":\"\"},{\"x\":1540137600000,\"y\":0.9085,\"equityReturn\":1.5878,\"unitMoney\":\"\"},{\"x\":1540224000000,\"y\":0.9005,\"equityReturn\":-0.8806,\"unitMoney\":\"\"},{\"x\":1540310400000,\"y\":0.9018,\"equityReturn\":0.1444,\"unitMoney\":\"\"},{\"x\":1540396800000,\"y\":0.9013,\"equityReturn\":-0.0554,\"unitMoney\":\"\"},{\"x\":1540483200000,\"y\":0.9042,\"equityReturn\":0.3218,\"unitMoney\":\"\"},{\"x\":1540742400000,\"y\":0.8956,\"equityReturn\":-0.9511,\"unitMoney\":\"\"},{\"x\":1540828800000,\"y\":0.8971,\"equityReturn\":0.1675,\"unitMoney\":\"\"},{\"x\":1540915200000,\"y\":0.9019,\"equityReturn\":0.5351,\"unitMoney\":\"\"},{\"x\":1541001600000,\"y\":0.9005,\"equityReturn\":-0.1552,\"unitMoney\":\"\"},{\"x\":1541088000000,\"y\":0.912,\"equityReturn\":1.2771,\"unitMoney\":\"\"},{\"x\":1541347200000,\"y\":0.9068,\"equityReturn\":-0.5702,\"unitMoney\":\"\"},{\"x\":1541433600000,\"y\":0.9036,\"equityReturn\":-0.3529,\"unitMoney\":\"\"},{\"x\":1541520000000,\"y\":0.8997,\"equityReturn\":-0.4316,\"unitMoney\":\"\"},{\"x\":1541606400000,\"y\":0.8988,\"equityReturn\":-0.1,\"unitMoney\":\"\"},{\"x\":1541692800000,\"y\":0.8915,\"equityReturn\":-0.8122,\"unitMoney\":\"\"},{\"x\":1541952000000,\"y\":0.8959,\"equityReturn\":0.4936,\"unitMoney\":\"\"},{\"x\":1542038400000,\"y\":0.8982,\"equityReturn\":0.2567,\"unitMoney\":\"\"},{\"x\":1542124800000,\"y\":0.8964,\"equityReturn\":-0.2004,\"unitMoney\":\"\"},{\"x\":1542211200000,\"y\":0.9004,\"equityReturn\":0.4462,\"unitMoney\":\"\"},{\"x\":1542297600000,\"y\":0.9009,\"equityReturn\":0.0555,\"unitMoney\":\"\"},{\"x\":1542556800000,\"y\":0.9022,\"equityReturn\":0.1443,\"unitMoney\":\"\"},{\"x\":1542643200000,\"y\":0.8895,\"equityReturn\":-1.4077,\"unitMoney\":\"\"},{\"x\":1542729600000,\"y\":0.8888,\"equityReturn\":-0.0787,\"unitMoney\":\"\"},{\"x\":1542816000000,\"y\":0.8859,\"equityReturn\":-0.3263,\"unitMoney\":\"\"},{\"x\":1542902400000,\"y\":0.8742,\"equityReturn\":-1.3207,\"unitMoney\":\"\"},{\"x\":1543161600000,\"y\":0.8716,\"equityReturn\":-0.2974,\"unitMoney\":\"\"},{\"x\":1543248000000,\"y\":0.8711,\"equityReturn\":-0.0574,\"unitMoney\":\"\"},{\"x\":1543334400000,\"y\":0.8765,\"equityReturn\":0.6199,\"unitMoney\":\"\"},{\"x\":1543420800000,\"y\":0.8702,\"equityReturn\":-0.7188,\"unitMoney\":\"\"},{\"x\":1543507200000,\"y\":0.8739,\"equityReturn\":0.4252,\"unitMoney\":\"\"},{\"x\":1543766400000,\"y\":0.8854,\"equityReturn\":1.3159,\"unitMoney\":\"\"},{\"x\":1543852800000,\"y\":0.8857,\"equityReturn\":0.0339,\"unitMoney\":\"\"},{\"x\":1543939200000,\"y\":0.8843,\"equityReturn\":-0.1581,\"unitMoney\":\"\"},{\"x\":1544025600000,\"y\":0.8767,\"equityReturn\":-0.8594,\"unitMoney\":\"\"},{\"x\":1544112000000,\"y\":0.8784,\"equityReturn\":0.1939,\"unitMoney\":\"\"},{\"x\":1544371200000,\"y\":0.8733,\"equityReturn\":-0.5806,\"unitMoney\":\"\"},{\"x\":1544457600000,\"y\":0.8756,\"equityReturn\":0.2634,\"unitMoney\":\"\"},{\"x\":1544544000000,\"y\":0.8761,\"equityReturn\":0.0571,\"unitMoney\":\"\"},{\"x\":1544630400000,\"y\":0.8814,\"equityReturn\":0.605,\"unitMoney\":\"\"},{\"x\":1544716800000,\"y\":0.8738,\"equityReturn\":-0.8623,\"unitMoney\":\"\"},{\"x\":1544976000000,\"y\":0.8704,\"equityReturn\":-0.3891,\"unitMoney\":\"\"},{\"x\":1545062400000,\"y\":0.8619,\"equityReturn\":-0.9766,\"unitMoney\":\"\"},{\"x\":1545148800000,\"y\":0.8555,\"equityReturn\":-0.7425,\"unitMoney\":\"\"},{\"x\":1545235200000,\"y\":0.8497,\"equityReturn\":-0.678,\"unitMoney\":\"\"},{\"x\":1545321600000,\"y\":0.8442,\"equityReturn\":-0.6473,\"unitMoney\":\"\"},{\"x\":1545580800000,\"y\":0.8458,\"equityReturn\":0.1895,\"unitMoney\":\"\"},{\"x\":1545667200000,\"y\":0.8412,\"equityReturn\":-0.5439,\"unitMoney\":\"\"},{\"x\":1545753600000,\"y\":0.8397,\"equityReturn\":-0.1783,\"unitMoney\":\"\"},{\"x\":1545840000000,\"y\":0.8369,\"equityReturn\":-0.3335,\"unitMoney\":\"\"},{\"x\":1545926400000,\"y\":0.8404,\"equityReturn\":0.4182,\"unitMoney\":\"\"},{\"x\":1546185600000,\"y\":0.8401,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1546358400000,\"y\":0.8283,\"equityReturn\":-1.4398,\"unitMoney\":\"\"},{\"x\":1546444800000,\"y\":0.8323,\"equityReturn\":0.4829,\"unitMoney\":\"\"},{\"x\":1546531200000,\"y\":0.8463,\"equityReturn\":1.6821,\"unitMoney\":\"\"},{\"x\":1546790400000,\"y\":0.8539,\"equityReturn\":0.898,\"unitMoney\":\"\"},{\"x\":1546876800000,\"y\":0.8527,\"equityReturn\":-0.1405,\"unitMoney\":\"\"},{\"x\":1546963200000,\"y\":0.8591,\"equityReturn\":0.7506,\"unitMoney\":\"\"},{\"x\":1547049600000,\"y\":0.8616,\"equityReturn\":0.291,\"unitMoney\":\"\"},{\"x\":1547136000000,\"y\":0.864,\"equityReturn\":0.2786,\"unitMoney\":\"\"},{\"x\":1547395200000,\"y\":0.8634,\"equityReturn\":-0.0694,\"unitMoney\":\"\"},{\"x\":1547481600000,\"y\":0.8689,\"equityReturn\":0.637,\"unitMoney\":\"\"},{\"x\":1547568000000,\"y\":0.8676,\"equityReturn\":-0.1496,\"unitMoney\":\"\"},{\"x\":1547654400000,\"y\":0.866,\"equityReturn\":-0.1844,\"unitMoney\":\"\"},{\"x\":1547740800000,\"y\":0.8799,\"equityReturn\":1.6051,\"unitMoney\":\"\"},{\"x\":1548000000000,\"y\":0.8808,\"equityReturn\":0.1023,\"unitMoney\":\"\"},{\"x\":1548086400000,\"y\":0.8742,\"equityReturn\":-0.7493,\"unitMoney\":\"\"},{\"x\":1548172800000,\"y\":0.8732,\"equityReturn\":-0.1144,\"unitMoney\":\"\"},{\"x\":1548259200000,\"y\":0.8786,\"equityReturn\":0.6184,\"unitMoney\":\"\"},{\"x\":1548345600000,\"y\":0.8902,\"equityReturn\":1.3203,\"unitMoney\":\"\"},{\"x\":1548604800000,\"y\":0.8873,\"equityReturn\":-0.3258,\"unitMoney\":\"\"},{\"x\":1548691200000,\"y\":0.8912,\"equityReturn\":0.4395,\"unitMoney\":\"\"},{\"x\":1548777600000,\"y\":0.8921,\"equityReturn\":0.101,\"unitMoney\":\"\"},{\"x\":1548864000000,\"y\":0.893,\"equityReturn\":0.1009,\"unitMoney\":\"\"},{\"x\":1548950400000,\"y\":0.9044,\"equityReturn\":1.2766,\"unitMoney\":\"\"},{\"x\":1549814400000,\"y\":0.9107,\"equityReturn\":0.6966,\"unitMoney\":\"\"},{\"x\":1549900800000,\"y\":0.9136,\"equityReturn\":0.3184,\"unitMoney\":\"\"},{\"x\":1549987200000,\"y\":0.9253,\"equityReturn\":1.2806,\"unitMoney\":\"\"},{\"x\":1550073600000,\"y\":0.9269,\"equityReturn\":0.1729,\"unitMoney\":\"\"},{\"x\":1550160000000,\"y\":0.9167,\"equityReturn\":-1.1004,\"unitMoney\":\"\"},{\"x\":1550419200000,\"y\":0.9358,\"equityReturn\":2.0836,\"unitMoney\":\"\"},{\"x\":1550505600000,\"y\":0.9327,\"equityReturn\":-0.3313,\"unitMoney\":\"\"},{\"x\":1550592000000,\"y\":0.9314,\"equityReturn\":-0.1394,\"unitMoney\":\"\"},{\"x\":1550678400000,\"y\":0.9309,\"equityReturn\":-0.0537,\"unitMoney\":\"\"},{\"x\":1550764800000,\"y\":0.9456,\"equityReturn\":1.5791,\"unitMoney\":\"\"},{\"x\":1551024000000,\"y\":0.9881,\"equityReturn\":4.4945,\"unitMoney\":\"\"},{\"x\":1551110400000,\"y\":0.9738,\"equityReturn\":-1.4472,\"unitMoney\":\"\"},{\"x\":1551196800000,\"y\":0.9706,\"equityReturn\":-0.3286,\"unitMoney\":\"\"},{\"x\":1551283200000,\"y\":0.9694,\"equityReturn\":-0.1236,\"unitMoney\":\"\"},{\"x\":1551369600000,\"y\":0.9841,\"equityReturn\":1.5164,\"unitMoney\":\"\"},{\"x\":1551628800000,\"y\":0.9958,\"equityReturn\":1.1889,\"unitMoney\":\"\"},{\"x\":1551715200000,\"y\":1.0078,\"equityReturn\":1.2051,\"unitMoney\":\"\"},{\"x\":1551801600000,\"y\":1.0157,\"equityReturn\":0.7839,\"unitMoney\":\"\"},{\"x\":1551888000000,\"y\":1.0136,\"equityReturn\":-0.2068,\"unitMoney\":\"\"},{\"x\":1551974400000,\"y\":0.9774,\"equityReturn\":-3.5714,\"unitMoney\":\"\"},{\"x\":1552233600000,\"y\":0.9973,\"equityReturn\":2.036,\"unitMoney\":\"\"},{\"x\":1552320000000,\"y\":1.0086,\"equityReturn\":1.1331,\"unitMoney\":\"\"},{\"x\":1552406400000,\"y\":0.99,\"equityReturn\":-1.8441,\"unitMoney\":\"\"},{\"x\":1552492800000,\"y\":0.9763,\"equityReturn\":-1.3838,\"unitMoney\":\"\"},{\"x\":1552579200000,\"y\":0.985,\"equityReturn\":0.8911,\"unitMoney\":\"\"},{\"x\":1552838400000,\"y\":1.0099,\"equityReturn\":2.5279,\"unitMoney\":\"\"},{\"x\":1552924800000,\"y\":1.0139,\"equityReturn\":0.3961,\"unitMoney\":\"\"},{\"x\":1553011200000,\"y\":1.0113,\"equityReturn\":-0.2564,\"unitMoney\":\"\"},{\"x\":1553097600000,\"y\":1.0161,\"equityReturn\":0.4746,\"unitMoney\":\"\"},{\"x\":1553184000000,\"y\":1.0123,\"equityReturn\":-0.374,\"unitMoney\":\"\"},{\"x\":1553443200000,\"y\":0.9953,\"equityReturn\":-1.6793,\"unitMoney\":\"\"},{\"x\":1553529600000,\"y\":0.9757,\"equityReturn\":-1.9693,\"unitMoney\":\"\"},{\"x\":1553616000000,\"y\":0.9799,\"equityReturn\":0.4305,\"unitMoney\":\"\"},{\"x\":1553702400000,\"y\":0.9743,\"equityReturn\":-0.5715,\"unitMoney\":\"\"},{\"x\":1553788800000,\"y\":0.9967,\"equityReturn\":2.2991,\"unitMoney\":\"\"},{\"x\":1554048000000,\"y\":1.035,\"equityReturn\":3.8427,\"unitMoney\":\"\"},{\"x\":1554134400000,\"y\":1.0404,\"equityReturn\":0.5217,\"unitMoney\":\"\"},{\"x\":1554220800000,\"y\":1.0472,\"equityReturn\":0.6536,\"unitMoney\":\"\"},{\"x\":1554307200000,\"y\":1.059,\"equityReturn\":1.1268,\"unitMoney\":\"\"},{\"x\":1554652800000,\"y\":1.0665,\"equityReturn\":0.7082,\"unitMoney\":\"\"},{\"x\":1554739200000,\"y\":1.0494,\"equityReturn\":-1.6034,\"unitMoney\":\"\"},{\"x\":1554825600000,\"y\":1.0413,\"equityReturn\":-0.7719,\"unitMoney\":\"\"},{\"x\":1554912000000,\"y\":1.023,\"equityReturn\":-1.7574,\"unitMoney\":\"\"},{\"x\":1554998400000,\"y\":1.0199,\"equityReturn\":-0.303,\"unitMoney\":\"\"},{\"x\":1555257600000,\"y\":1.0147,\"equityReturn\":-0.5099,\"unitMoney\":\"\"},{\"x\":1555344000000,\"y\":1.0288,\"equityReturn\":1.3896,\"unitMoney\":\"\"},{\"x\":1555430400000,\"y\":1.0317,\"equityReturn\":0.2819,\"unitMoney\":\"\"},{\"x\":1555516800000,\"y\":1.0288,\"equityReturn\":-0.2811,\"unitMoney\":\"\"},{\"x\":1555603200000,\"y\":1.0331,\"equityReturn\":0.418,\"unitMoney\":\"\"},{\"x\":1555862400000,\"y\":1.0138,\"equityReturn\":-1.8682,\"unitMoney\":\"\"},{\"x\":1555948800000,\"y\":1.0037,\"equityReturn\":-0.9963,\"unitMoney\":\"\"},{\"x\":1556035200000,\"y\":1.0016,\"equityReturn\":-0.2092,\"unitMoney\":\"\"},{\"x\":1556121600000,\"y\":0.9795,\"equityReturn\":-2.2065,\"unitMoney\":\"\"},{\"x\":1556208000000,\"y\":0.9646,\"equityReturn\":-1.5212,\"unitMoney\":\"\"},{\"x\":1556467200000,\"y\":0.9508,\"equityReturn\":-1.4306,\"unitMoney\":\"\"},{\"x\":1556553600000,\"y\":0.9644,\"equityReturn\":1.4304,\"unitMoney\":\"\"},{\"x\":1557072000000,\"y\":0.9211,\"equityReturn\":-4.4898,\"unitMoney\":\"\"},{\"x\":1557158400000,\"y\":0.9274,\"equityReturn\":0.684,\"unitMoney\":\"\"},{\"x\":1557244800000,\"y\":0.9267,\"equityReturn\":-0.0755,\"unitMoney\":\"\"},{\"x\":1557331200000,\"y\":0.9233,\"equityReturn\":-0.3669,\"unitMoney\":\"\"},{\"x\":1557417600000,\"y\":0.9473,\"equityReturn\":2.5994,\"unitMoney\":\"\"},{\"x\":1557676800000,\"y\":0.9424,\"equityReturn\":-0.5173,\"unitMoney\":\"\"},{\"x\":1557763200000,\"y\":0.9396,\"equityReturn\":-0.2971,\"unitMoney\":\"\"},{\"x\":1557849600000,\"y\":0.9525,\"equityReturn\":1.3729,\"unitMoney\":\"\"},{\"x\":1557936000000,\"y\":0.9551,\"equityReturn\":0.273,\"unitMoney\":\"\"},{\"x\":1558022400000,\"y\":0.9375,\"equityReturn\":-1.8427,\"unitMoney\":\"\"},{\"x\":1558281600000,\"y\":0.93,\"equityReturn\":-0.8,\"unitMoney\":\"\"},{\"x\":1558368000000,\"y\":0.9372,\"equityReturn\":0.7742,\"unitMoney\":\"\"},{\"x\":1558454400000,\"y\":0.9365,\"equityReturn\":-0.0747,\"unitMoney\":\"\"},{\"x\":1558540800000,\"y\":0.9258,\"equityReturn\":-1.1426,\"unitMoney\":\"\"},{\"x\":1558627200000,\"y\":0.9206,\"equityReturn\":-0.5617,\"unitMoney\":\"\"},{\"x\":1558886400000,\"y\":0.928,\"equityReturn\":0.8038,\"unitMoney\":\"\"},{\"x\":1558972800000,\"y\":0.9303,\"equityReturn\":0.2478,\"unitMoney\":\"\"},{\"x\":1559059200000,\"y\":0.9315,\"equityReturn\":0.129,\"unitMoney\":\"\"},{\"x\":1559145600000,\"y\":0.9219,\"equityReturn\":-1.0306,\"unitMoney\":\"\"},{\"x\":1559232000000,\"y\":0.9195,\"equityReturn\":-0.2603,\"unitMoney\":\"\"},{\"x\":1559491200000,\"y\":0.9099,\"equityReturn\":-1.044,\"unitMoney\":\"\"},{\"x\":1559577600000,\"y\":0.8968,\"equityReturn\":-1.4397,\"unitMoney\":\"\"},{\"x\":1559664000000,\"y\":0.8945,\"equityReturn\":-0.2565,\"unitMoney\":\"\"},{\"x\":1559750400000,\"y\":0.8873,\"equityReturn\":-0.8049,\"unitMoney\":\"\"},{\"x\":1560096000000,\"y\":0.8937,\"equityReturn\":0.7213,\"unitMoney\":\"\"},{\"x\":1560182400000,\"y\":0.9166,\"equityReturn\":2.5624,\"unitMoney\":\"\"},{\"x\":1560268800000,\"y\":0.9122,\"equityReturn\":-0.48,\"unitMoney\":\"\"},{\"x\":1560355200000,\"y\":0.909,\"equityReturn\":-0.3508,\"unitMoney\":\"\"},{\"x\":1560441600000,\"y\":0.9007,\"equityReturn\":-0.9131,\"unitMoney\":\"\"},{\"x\":1560700800000,\"y\":0.9001,\"equityReturn\":-0.0666,\"unitMoney\":\"\"},{\"x\":1560787200000,\"y\":0.8998,\"equityReturn\":-0.0333,\"unitMoney\":\"\"},{\"x\":1560873600000,\"y\":0.9097,\"equityReturn\":1.1002,\"unitMoney\":\"\"},{\"x\":1560960000000,\"y\":0.9305,\"equityReturn\":2.2865,\"unitMoney\":\"\"},{\"x\":1561046400000,\"y\":0.9387,\"equityReturn\":0.8812,\"unitMoney\":\"\"},{\"x\":1561305600000,\"y\":0.9343,\"equityReturn\":-0.4687,\"unitMoney\":\"\"},{\"x\":1561392000000,\"y\":0.9279,\"equityReturn\":-0.685,\"unitMoney\":\"\"},{\"x\":1561478400000,\"y\":0.9287,\"equityReturn\":0.0862,\"unitMoney\":\"\"},{\"x\":1561564800000,\"y\":0.9364,\"equityReturn\":0.8291,\"unitMoney\":\"\"},{\"x\":1561651200000,\"y\":0.9309,\"equityReturn\":-0.5874,\"unitMoney\":\"\"},{\"x\":1561824000000,\"y\":0.9308,\"equityReturn\":0,\"unitMoney\":\"\"},{\"x\":1561910400000,\"y\":0.9474,\"equityReturn\":1.7725,\"unitMoney\":\"\"},{\"x\":1561996800000,\"y\":0.9495,\"equityReturn\":0.2217,\"unitMoney\":\"\"},{\"x\":1562083200000,\"y\":0.9442,\"equityReturn\":-0.5582,\"unitMoney\":\"\"},{\"x\":1562169600000,\"y\":0.9437,\"equityReturn\":-0.053,\"unitMoney\":\"\"},{\"x\":1562256000000,\"y\":0.9441,\"equityReturn\":0.0424,\"unitMoney\":\"\"},{\"x\":1562515200000,\"y\":0.9271,\"equityReturn\":-1.8007,\"unitMoney\":\"\"},{\"x\":1562601600000,\"y\":0.9268,\"equityReturn\":-0.0324,\"unitMoney\":\"\"},{\"x\":1562688000000,\"y\":0.9241,\"equityReturn\":-0.2913,\"unitMoney\":\"\"},{\"x\":1562774400000,\"y\":0.9222,\"equityReturn\":-0.2056,\"unitMoney\":\"\"},{\"x\":1562860800000,\"y\":0.9273,\"equityReturn\":0.553,\"unitMoney\":\"\"},{\"x\":1563120000000,\"y\":0.9333,\"equityReturn\":0.647,\"unitMoney\":\"\"},{\"x\":1563206400000,\"y\":0.9337,\"equityReturn\":0.0429,\"unitMoney\":\"\"},{\"x\":1563292800000,\"y\":0.9367,\"equityReturn\":0.3213,\"unitMoney\":\"\"},{\"x\":1563379200000,\"y\":0.9282,\"equityReturn\":-0.9074,\"unitMoney\":\"\"},{\"x\":1563465600000,\"y\":0.9332,\"equityReturn\":0.5387,\"unitMoney\":\"\"},{\"x\":1563724800000,\"y\":0.9233,\"equityReturn\":-1.0609,\"unitMoney\":\"\"},{\"x\":1563811200000,\"y\":0.9266,\"equityReturn\":0.3574,\"unitMoney\":\"\"},{\"x\":1563897600000,\"y\":0.9316,\"equityReturn\":0.5396,\"unitMoney\":\"\"},{\"x\":1563984000000,\"y\":0.9384,\"equityReturn\":0.7299,\"unitMoney\":\"\"},{\"x\":1564070400000,\"y\":0.9446,\"equityReturn\":0.6607,\"unitMoney\":\"\"},{\"x\":1564329600000,\"y\":0.9456,\"equityReturn\":0.1059,\"unitMoney\":\"\"},{\"x\":1564416000000,\"y\":0.9493,\"equityReturn\":0.3913,\"unitMoney\":\"\"},{\"x\":1564502400000,\"y\":0.9471,\"equityReturn\":-0.2317,\"unitMoney\":\"\"},{\"x\":1564588800000,\"y\":0.9445,\"equityReturn\":-0.2745,\"unitMoney\":\"\"},{\"x\":1564675200000,\"y\":0.9368,\"equityReturn\":-0.8152,\"unitMoney\":\"\"},{\"x\":1564934400000,\"y\":0.9282,\"equityReturn\":-0.918,\"unitMoney\":\"\"},{\"x\":1565020800000,\"y\":0.9201,\"equityReturn\":-0.8727,\"unitMoney\":\"\"},{\"x\":1565107200000,\"y\":0.9231,\"equityReturn\":0.3261,\"unitMoney\":\"\"},{\"x\":1565193600000,\"y\":0.9269,\"equityReturn\":0.4117,\"unitMoney\":\"\"},{\"x\":1565280000000,\"y\":0.922,\"equityReturn\":-0.5286,\"unitMoney\":\"\"},{\"x\":1565539200000,\"y\":0.9294,\"equityReturn\":0.8026,\"unitMoney\":\"\"},{\"x\":1565625600000,\"y\":0.9257,\"equityReturn\":-0.3981,\"unitMoney\":\"\"},{\"x\":1565712000000,\"y\":0.9288,\"equityReturn\":0.3349,\"unitMoney\":\"\"},{\"x\":1565798400000,\"y\":0.9305,\"equityReturn\":0.183,\"unitMoney\":\"\"},{\"x\":1565884800000,\"y\":0.9338,\"equityReturn\":0.3546,\"unitMoney\":\"\"},{\"x\":1566144000000,\"y\":0.9514,\"equityReturn\":1.8848,\"unitMoney\":\"\"},{\"x\":1566230400000,\"y\":0.952,\"equityReturn\":0.0631,\"unitMoney\":\"\"},{\"x\":1566316800000,\"y\":0.9533,\"equityReturn\":0.1366,\"unitMoney\":\"\"},{\"x\":1566403200000,\"y\":0.9523,\"equityReturn\":-0.1049,\"unitMoney\":\"\"},{\"x\":1566489600000,\"y\":0.9527,\"equityReturn\":0.042,\"unitMoney\":\"\"},{\"x\":1566748800000,\"y\":0.9447,\"equityReturn\":-0.8397,\"unitMoney\":\"\"},{\"x\":1566835200000,\"y\":0.9535,\"equityReturn\":0.9315,\"unitMoney\":\"\"},{\"x\":1566921600000,\"y\":0.9531,\"equityReturn\":-0.042,\"unitMoney\":\"\"},{\"x\":1567008000000,\"y\":0.9539,\"equityReturn\":0.0839,\"unitMoney\":\"\"},{\"x\":1567094400000,\"y\":0.952,\"equityReturn\":-0.1992,\"unitMoney\":\"\"},{\"x\":1567353600000,\"y\":0.9636,\"equityReturn\":1.2185,\"unitMoney\":\"\"},{\"x\":1567440000000,\"y\":0.9688,\"equityReturn\":0.5396,\"unitMoney\":\"\"},{\"x\":1567526400000,\"y\":0.974,\"equityReturn\":0.5367,\"unitMoney\":\"\"},{\"x\":1567612800000,\"y\":0.984,\"equityReturn\":1.0267,\"unitMoney\":\"\"},{\"x\":1567699200000,\"y\":0.9873,\"equityReturn\":0.3354,\"unitMoney\":\"\"},{\"x\":1567958400000,\"y\":1.0028,\"equityReturn\":1.5699,\"unitMoney\":\"\"},{\"x\":1568044800000,\"y\":1.0002,\"equityReturn\":-0.2593,\"unitMoney\":\"\"},{\"x\":1568131200000,\"y\":0.9959,\"equityReturn\":-0.4299,\"unitMoney\":\"\"},{\"x\":1568217600000,\"y\":0.9977,\"equityReturn\":0.1807,\"unitMoney\":\"\"},{\"x\":1568563200000,\"y\":0.9923,\"equityReturn\":-0.5412,\"unitMoney\":\"\"},{\"x\":1568649600000,\"y\":0.9768,\"equityReturn\":-1.562,\"unitMoney\":\"\"},{\"x\":1568736000000,\"y\":0.9752,\"equityReturn\":-0.1638,\"unitMoney\":\"\"},{\"x\":1568822400000,\"y\":0.9805,\"equityReturn\":0.5435,\"unitMoney\":\"\"},{\"x\":1568908800000,\"y\":0.9842,\"equityReturn\":0.3774,\"unitMoney\":\"\"},{\"x\":1569168000000,\"y\":0.9795,\"equityReturn\":-0.4775,\"unitMoney\":\"\"},{\"x\":1569254400000,\"y\":0.9811,\"equityReturn\":0.1633,\"unitMoney\":\"\"},{\"x\":1569340800000,\"y\":0.9744,\"equityReturn\":-0.6829,\"unitMoney\":\"\"},{\"x\":1569427200000,\"y\":0.967,\"equityReturn\":-0.7594,\"unitMoney\":\"\"},{\"x\":1569513600000,\"y\":0.9684,\"equityReturn\":0.1448,\"unitMoney\":\"\"},{\"x\":1569772800000,\"y\":0.961,\"equityReturn\":-0.7641,\"unitMoney\":\"\"},{\"x\":1570464000000,\"y\":0.9632,\"equityReturn\":0.2289,\"unitMoney\":\"\"},{\"x\":1570550400000,\"y\":0.9711,\"equityReturn\":0.8202,\"unitMoney\":\"\"},{\"x\":1570636800000,\"y\":0.9791,\"equityReturn\":0.8238,\"unitMoney\":\"\"},{\"x\":1570723200000,\"y\":0.9878,\"equityReturn\":0.8886,\"unitMoney\":\"\"},{\"x\":1570982400000,\"y\":1.0034,\"equityReturn\":1.5793,\"unitMoney\":\"\"},{\"x\":1571068800000,\"y\":0.9964,\"equityReturn\":-0.6976,\"unitMoney\":\"\"},{\"x\":1571155200000,\"y\":0.9916,\"equityReturn\":-0.4817,\"unitMoney\":\"\"},{\"x\":1571241600000,\"y\":0.9898,\"equityReturn\":-0.1815,\"unitMoney\":\"\"},{\"x\":1571328000000,\"y\":0.9812,\"equityReturn\":-0.8689,\"unitMoney\":\"\"},{\"x\":1571587200000,\"y\":0.9806,\"equityReturn\":-0.0611,\"unitMoney\":\"\"},{\"x\":1571673600000,\"y\":0.9833,\"equityReturn\":0.2753,\"unitMoney\":\"\"},{\"x\":1571760000000,\"y\":0.9812,\"equityReturn\":-0.2136,\"unitMoney\":\"\"},{\"x\":1571846400000,\"y\":0.9822,\"equityReturn\":0.1019,\"unitMoney\":\"\"},{\"x\":1571932800000,\"y\":0.9845,\"equityReturn\":0.2342,\"unitMoney\":\"\"},{\"x\":1572192000000,\"y\":0.9888,\"equityReturn\":0.4368,\"unitMoney\":\"\"},{\"x\":1572278400000,\"y\":0.9891,\"equityReturn\":0.0303,\"unitMoney\":\"\"},{\"x\":1572364800000,\"y\":0.986,\"equityReturn\":-0.3134,\"unitMoney\":\"\"},{\"x\":1572451200000,\"y\":0.9795,\"equityReturn\":-0.6592,\"unitMoney\":\"\"},{\"x\":1572537600000,\"y\":0.9871,\"equityReturn\":0.7759,\"unitMoney\":\"\"},{\"x\":1572796800000,\"y\":0.9923,\"equityReturn\":0.5268,\"unitMoney\":\"\"},{\"x\":1572883200000,\"y\":1.0044,\"equityReturn\":1.2194,\"unitMoney\":\"\"},{\"x\":1572969600000,\"y\":1.0017,\"equityReturn\":-0.2688,\"unitMoney\":\"\"},{\"x\":1573056000000,\"y\":1.0042,\"equityReturn\":0.2496,\"unitMoney\":\"\"},{\"x\":1573142400000,\"y\":1.0015,\"equityReturn\":-0.2689,\"unitMoney\":\"\"},{\"x\":1573401600000,\"y\":0.9917,\"equityReturn\":-0.9785,\"unitMoney\":\"\"},{\"x\":1573488000000,\"y\":0.9909,\"equityReturn\":-0.0807,\"unitMoney\":\"\"},{\"x\":1573574400000,\"y\":0.991,\"equityReturn\":0.0101,\"unitMoney\":\"\"},{\"x\":1573660800000,\"y\":0.9904,\"equityReturn\":-0.0605,\"unitMoney\":\"\"},{\"x\":1573747200000,\"y\":0.9853,\"equityReturn\":-0.5149,\"unitMoney\":\"\"},{\"x\":1574006400000,\"y\":0.9927,\"equityReturn\":0.751,\"unitMoney\":\"\"},{\"x\":1574092800000,\"y\":1.002,\"equityReturn\":0.9368,\"unitMoney\":\"\"},{\"x\":1574179200000,\"y\":0.9979,\"equityReturn\":-0.4092,\"unitMoney\":\"\"},{\"x\":1574265600000,\"y\":0.9945,\"equityReturn\":-0.3407,\"unitMoney\":\"\"},{\"x\":1574352000000,\"y\":0.9886,\"equityReturn\":-0.5933,\"unitMoney\":\"\"},{\"x\":1574611200000,\"y\":0.9921,\"equityReturn\":0.354,\"unitMoney\":\"\"},{\"x\":1574697600000,\"y\":0.9919,\"equityReturn\":-0.0202,\"unitMoney\":\"\"},{\"x\":1574784000000,\"y\":0.9896,\"equityReturn\":-0.2319,\"unitMoney\":\"\"},{\"x\":1574870400000,\"y\":0.9849,\"equityReturn\":-0.4749,\"unitMoney\":\"\"},{\"x\":1574956800000,\"y\":0.9822,\"equityReturn\":-0.2741,\"unitMoney\":\"\"},{\"x\":1575216000000,\"y\":0.9825,\"equityReturn\":0.0305,\"unitMoney\":\"\"},{\"x\":1575302400000,\"y\":0.9841,\"equityReturn\":0.1628,\"unitMoney\":\"\"},{\"x\":1575388800000,\"y\":0.9839,\"equityReturn\":-0.0203,\"unitMoney\":\"\"},{\"x\":1575475200000,\"y\":0.9898,\"equityReturn\":0.5997,\"unitMoney\":\"\"},{\"x\":1575561600000,\"y\":0.9942,\"equityReturn\":0.4445,\"unitMoney\":\"\"},{\"x\":1575820800000,\"y\":0.9971,\"equityReturn\":0.2917,\"unitMoney\":\"\"},{\"x\":1575907200000,\"y\":1.0005,\"equityReturn\":0.341,\"unitMoney\":\"\"},{\"x\":1575993600000,\"y\":0.9998,\"equityReturn\":-0.07,\"unitMoney\":\"\"},{\"x\":1576080000000,\"y\":1.0034,\"equityReturn\":0.3601,\"unitMoney\":\"\"},{\"x\":1576166400000,\"y\":1.0136,\"equityReturn\":1.0165,\"unitMoney\":\"\"},{\"x\":1576425600000,\"y\":1.0232,\"equityReturn\":0.9471,\"unitMoney\":\"\"},{\"x\":1576512000000,\"y\":1.039,\"equityReturn\":1.5442,\"unitMoney\":\"\"},{\"x\":1576598400000,\"y\":1.0431,\"equityReturn\":0.3946,\"unitMoney\":\"\"},{\"x\":1576684800000,\"y\":1.0419,\"equityReturn\":-0.115,\"unitMoney\":\"\"},{\"x\":1576771200000,\"y\":1.0387,\"equityReturn\":-0.3071,\"unitMoney\":\"\"},{\"x\":1577030400000,\"y\":1.0323,\"equityReturn\":-0.6162,\"unitMoney\":\"\"},{\"x\":1577116800000,\"y\":1.0462,\"equityReturn\":1.3465,\"unitMoney\":\"\"},{\"x\":1577203200000,\"y\":1.0476,\"equityReturn\":0.1338,\"unitMoney\":\"\"},{\"x\":1577289600000,\"y\":1.0585,\"equityReturn\":1.0405,\"unitMoney\":\"\"},{\"x\":1577376000000,\"y\":1.0587,\"equityReturn\":0.0189,\"unitMoney\":\"\"},{\"x\":1577635200000,\"y\":1.0665,\"equityReturn\":0.7368,\"unitMoney\":\"\"},{\"x\":1577721600000,\"y\":1.0746,\"equityReturn\":0.7595,\"unitMoney\":\"\"},{\"x\":1577894400000,\"y\":1.0903,\"equityReturn\":1.461,\"unitMoney\":\"\"},{\"x\":1577980800000,\"y\":1.0941,\"equityReturn\":0.3485,\"unitMoney\":\"\"},{\"x\":1578240000000,\"y\":1.0978,\"equityReturn\":0.3382,\"unitMoney\":\"\"},{\"x\":1578326400000,\"y\":1.1022,\"equityReturn\":0.4008,\"unitMoney\":\"\"},{\"x\":1578412800000,\"y\":1.0935,\"equityReturn\":-0.7893,\"unitMoney\":\"\"},{\"x\":1578499200000,\"y\":1.0992,\"equityReturn\":0.5213,\"unitMoney\":\"\"},{\"x\":1578585600000,\"y\":1.0942,\"equityReturn\":-0.4549,\"unitMoney\":\"\"},{\"x\":1578844800000,\"y\":1.1045,\"equityReturn\":0.9413,\"unitMoney\":\"\"},{\"x\":1578931200000,\"y\":1.1055,\"equityReturn\":0.0905,\"unitMoney\":\"\"},{\"x\":1579017600000,\"y\":1.0968,\"equityReturn\":-0.787,\"unitMoney\":\"\"},{\"x\":1579104000000,\"y\":1.0939,\"equityReturn\":-0.2644,\"unitMoney\":\"\"},{\"x\":1579190400000,\"y\":1.0953,\"equityReturn\":0.128,\"unitMoney\":\"\"},{\"x\":1579449600000,\"y\":1.1097,\"equityReturn\":1.3147,\"unitMoney\":\"\"},{\"x\":1579536000000,\"y\":1.0985,\"equityReturn\":-1.0093,\"unitMoney\":\"\"},{\"x\":1579622400000,\"y\":1.1111,\"equityReturn\":1.147,\"unitMoney\":\"\"},{\"x\":1579708800000,\"y\":1.0861,\"equityReturn\":-2.25,\"unitMoney\":\"\"},{\"x\":1580659200000,\"y\":1.0202,\"equityReturn\":-6.0676,\"unitMoney\":\"\"},{\"x\":1580745600000,\"y\":1.0523,\"equityReturn\":3.1464,\"unitMoney\":\"\"},{\"x\":1580832000000,\"y\":1.0683,\"equityReturn\":1.5205,\"unitMoney\":\"\"},{\"x\":1580918400000,\"y\":1.0839,\"equityReturn\":1.4603,\"unitMoney\":\"\"},{\"x\":1581004800000,\"y\":1.0921,\"equityReturn\":0.7565,\"unitMoney\":\"\"},{\"x\":1581264000000,\"y\":1.0963,\"equityReturn\":0.3846,\"unitMoney\":\"\"},{\"x\":1581350400000,\"y\":1.0984,\"equityReturn\":0.1916,\"unitMoney\":\"\"},{\"x\":1581436800000,\"y\":1.1186,\"equityReturn\":1.839,\"unitMoney\":\"\"},{\"x\":1581523200000,\"y\":1.1174,\"equityReturn\":-0.1073,\"unitMoney\":\"\"},{\"x\":1581609600000,\"y\":1.1217,\"equityReturn\":0.3848,\"unitMoney\":\"\"},{\"x\":1581868800000,\"y\":1.151,\"equityReturn\":2.6121,\"unitMoney\":\"\"},{\"x\":1581955200000,\"y\":1.1659,\"equityReturn\":1.2945,\"unitMoney\":\"\"},{\"x\":1582041600000,\"y\":1.161,\"equityReturn\":-0.4203,\"unitMoney\":\"\"},{\"x\":1582128000000,\"y\":1.1802,\"equityReturn\":1.6537,\"unitMoney\":\"\"},{\"x\":1582214400000,\"y\":1.1945,\"equityReturn\":1.2117,\"unitMoney\":\"\"},{\"x\":1582473600000,\"y\":1.2144,\"equityReturn\":1.666,\"unitMoney\":\"\"},{\"x\":1582560000000,\"y\":1.2198,\"equityReturn\":0.4447,\"unitMoney\":\"\"},{\"x\":1582646400000,\"y\":1.1851,\"equityReturn\":-2.8447,\"unitMoney\":\"\"},{\"x\":1582732800000,\"y\":1.1794,\"equityReturn\":-0.481,\"unitMoney\":\"\"},{\"x\":1582819200000,\"y\":1.1453,\"equityReturn\":-2.8913,\"unitMoney\":\"\"},{\"x\":1583078400000,\"y\":1.1643,\"equityReturn\":1.659,\"unitMoney\":\"\"},{\"x\":1583164800000,\"y\":1.1678,\"equityReturn\":0.3006,\"unitMoney\":\"\"},{\"x\":1583251200000,\"y\":1.1636,\"equityReturn\":-0.3597,\"unitMoney\":\"\"},{\"x\":1583337600000,\"y\":1.1775,\"equityReturn\":1.1946,\"unitMoney\":\"\"},{\"x\":1583424000000,\"y\":1.1722,\"equityReturn\":-0.4501,\"unitMoney\":\"\"},{\"x\":1583683200000,\"y\":1.1415,\"equityReturn\":-2.619,\"unitMoney\":\"\"},{\"x\":1583769600000,\"y\":1.1595,\"equityReturn\":1.5769,\"unitMoney\":\"\"},{\"x\":1583856000000,\"y\":1.1518,\"equityReturn\":-0.6641,\"unitMoney\":\"\"},{\"x\":1583942400000,\"y\":1.137,\"equityReturn\":-1.2849,\"unitMoney\":\"\"},{\"x\":1584028800000,\"y\":1.1314,\"equityReturn\":-0.4925,\"unitMoney\":\"\"},{\"x\":1584288000000,\"y\":1.0975,\"equityReturn\":-2.9963,\"unitMoney\":\"\"},{\"x\":1584374400000,\"y\":1.0916,\"equityReturn\":-0.5376,\"unitMoney\":\"\"},{\"x\":1584460800000,\"y\":1.0843,\"equityReturn\":-0.6687,\"unitMoney\":\"\"},{\"x\":1584547200000,\"y\":1.0794,\"equityReturn\":-0.4519,\"unitMoney\":\"\"},{\"x\":1584633600000,\"y\":1.1008,\"equityReturn\":1.9826,\"unitMoney\":\"\"},{\"x\":1584892800000,\"y\":1.0707,\"equityReturn\":-2.7344,\"unitMoney\":\"\"},{\"x\":1584979200000,\"y\":1.0818,\"equityReturn\":1.0367,\"unitMoney\":\"\"},{\"x\":1585065600000,\"y\":1.1029,\"equityReturn\":1.9505,\"unitMoney\":\"\"},{\"x\":1585152000000,\"y\":1.0949,\"equityReturn\":-0.7254,\"unitMoney\":\"\"},{\"x\":1585238400000,\"y\":1.0877,\"equityReturn\":-0.6576,\"unitMoney\":\"\"},{\"x\":1585497600000,\"y\":1.0773,\"equityReturn\":-0.9561,\"unitMoney\":\"\"},{\"x\":1585584000000,\"y\":1.0775,\"equityReturn\":0.0186,\"unitMoney\":\"\"},{\"x\":1585670400000,\"y\":1.0772,\"equityReturn\":-0.0278,\"unitMoney\":\"\"},{\"x\":1585756800000,\"y\":1.0825,\"equityReturn\":0.492,\"unitMoney\":\"\"},{\"x\":1585843200000,\"y\":1.0748,\"equityReturn\":-0.7113,\"unitMoney\":\"\"},{\"x\":1586188800000,\"y\":1.0947,\"equityReturn\":1.8515,\"unitMoney\":\"\"},{\"x\":1586275200000,\"y\":1.0944,\"equityReturn\":-0.0274,\"unitMoney\":\"\"},{\"x\":1586361600000,\"y\":1.0989,\"equityReturn\":0.4112,\"unitMoney\":\"\"},{\"x\":1586448000000,\"y\":1.0824,\"equityReturn\":-1.5015,\"unitMoney\":\"\"},{\"x\":1586707200000,\"y\":1.0735,\"equityReturn\":-0.8222,\"unitMoney\":\"\"},{\"x\":1586793600000,\"y\":1.0833,\"equityReturn\":0.9129,\"unitMoney\":\"\"},{\"x\":1586880000000,\"y\":1.0786,\"equityReturn\":-0.4339,\"unitMoney\":\"\"},{\"x\":1586966400000,\"y\":1.0822,\"equityReturn\":0.3338,\"unitMoney\":\"\"},{\"x\":1587052800000,\"y\":1.0867,\"equityReturn\":0.4158,\"unitMoney\":\"\"},{\"x\":1587312000000,\"y\":1.0935,\"equityReturn\":0.6257,\"unitMoney\":\"\"},{\"x\":1587398400000,\"y\":1.088,\"equityReturn\":-0.503,\"unitMoney\":\"\"},{\"x\":1587484800000,\"y\":1.096,\"equityReturn\":0.7353,\"unitMoney\":\"\"},{\"x\":1587571200000,\"y\":1.0971,\"equityReturn\":0.1004,\"unitMoney\":\"\"},{\"x\":1587657600000,\"y\":1.0884,\"equityReturn\":-0.793,\"unitMoney\":\"\"},{\"x\":1587916800000,\"y\":1.092,\"equityReturn\":0.3308,\"unitMoney\":\"\"},{\"x\":1588003200000,\"y\":1.0933,\"equityReturn\":0.119,\"unitMoney\":\"\"},{\"x\":1588089600000,\"y\":1.0921,\"equityReturn\":-0.1098,\"unitMoney\":\"\"},{\"x\":1588176000000,\"y\":1.1032,\"equityReturn\":1.0164,\"unitMoney\":\"\"},{\"x\":1588694400000,\"y\":1.1121,\"equityReturn\":0.8067,\"unitMoney\":\"\"},{\"x\":1588780800000,\"y\":1.1095,\"equityReturn\":-0.2338,\"unitMoney\":\"\"},{\"x\":1588867200000,\"y\":1.1156,\"equityReturn\":0.5498,\"unitMoney\":\"\"},{\"x\":1589126400000,\"y\":1.1128,\"equityReturn\":-0.251,\"unitMoney\":\"\"},{\"x\":1589212800000,\"y\":1.1093,\"equityReturn\":-0.3145,\"unitMoney\":\"\"},{\"x\":1589299200000,\"y\":1.1087,\"equityReturn\":-0.0541,\"unitMoney\":\"\"},{\"x\":1589385600000,\"y\":1.094,\"equityReturn\":-1.3259,\"unitMoney\":\"\"},{\"x\":1589472000000,\"y\":1.0958,\"equityReturn\":0.1645,\"unitMoney\":\"\"},{\"x\":1589731200000,\"y\":1.0857,\"equityReturn\":-0.9217,\"unitMoney\":\"\"},{\"x\":1589817600000,\"y\":1.0867,\"equityReturn\":0.0921,\"unitMoney\":\"\"},{\"x\":1589904000000,\"y\":1.0731,\"equityReturn\":-1.2515,\"unitMoney\":\"\"},{\"x\":1589990400000,\"y\":1.067,\"equityReturn\":-0.5684,\"unitMoney\":\"\"},{\"x\":1590076800000,\"y\":1.0564,\"equityReturn\":-0.9934,\"unitMoney\":\"\"},{\"x\":1590336000000,\"y\":1.0479,\"equityReturn\":-0.8046,\"unitMoney\":\"\"},{\"x\":1590422400000,\"y\":1.0591,\"equityReturn\":1.0688,\"unitMoney\":\"\"},{\"x\":1590508800000,\"y\":1.0607,\"equityReturn\":0.1511,\"unitMoney\":\"\"},{\"x\":1590595200000,\"y\":1.0614,\"equityReturn\":0.066,\"unitMoney\":\"\"},{\"x\":1590681600000,\"y\":1.0713,\"equityReturn\":0.9327,\"unitMoney\":\"\"},{\"x\":1590940800000,\"y\":1.0927,\"equityReturn\":1.9976,\"unitMoney\":\"\"},{\"x\":1591027200000,\"y\":1.0973,\"equityReturn\":0.421,\"unitMoney\":\"\"},{\"x\":1591113600000,\"y\":1.0946,\"equityReturn\":-0.2461,\"unitMoney\":\"\"},{\"x\":1591200000000,\"y\":1.0933,\"equityReturn\":-0.1188,\"unitMoney\":\"\"},{\"x\":1591286400000,\"y\":1.0906,\"equityReturn\":-0.247,\"unitMoney\":\"\"},{\"x\":1591545600000,\"y\":1.0913,\"equityReturn\":0.0642,\"unitMoney\":\"\"},{\"x\":1591632000000,\"y\":1.092,\"equityReturn\":0.0641,\"unitMoney\":\"\"},{\"x\":1591718400000,\"y\":1.0869,\"equityReturn\":-0.467,\"unitMoney\":\"\"},{\"x\":1591804800000,\"y\":1.0802,\"equityReturn\":-0.6164,\"unitMoney\":\"\"},{\"x\":1591891200000,\"y\":1.0808,\"equityReturn\":0.0555,\"unitMoney\":\"\"},{\"x\":1592150400000,\"y\":1.0769,\"equityReturn\":-0.3608,\"unitMoney\":\"\"},{\"x\":1592236800000,\"y\":1.0921,\"equityReturn\":1.4115,\"unitMoney\":\"\"},{\"x\":1592323200000,\"y\":1.0916,\"equityReturn\":-0.0458,\"unitMoney\":\"\"},{\"x\":1592409600000,\"y\":1.0963,\"equityReturn\":0.4306,\"unitMoney\":\"\"},{\"x\":1592496000000,\"y\":1.104,\"equityReturn\":0.7024,\"unitMoney\":\"\"},{\"x\":1592755200000,\"y\":1.103,\"equityReturn\":-0.0906,\"unitMoney\":\"\"},{\"x\":1592841600000,\"y\":1.1031,\"equityReturn\":0.0091,\"unitMoney\":\"\"},{\"x\":1592928000000,\"y\":1.1043,\"equityReturn\":0.1088,\"unitMoney\":\"\"},{\"x\":1593360000000,\"y\":1.0961,\"equityReturn\":-0.7426,\"unitMoney\":\"\"},{\"x\":1593446400000,\"y\":1.1067,\"equityReturn\":0.9671,\"unitMoney\":\"\"},{\"x\":1593532800000,\"y\":1.1235,\"equityReturn\":1.518,\"unitMoney\":\"\"},{\"x\":1593619200000,\"y\":1.1572,\"equityReturn\":2.9996,\"unitMoney\":\"\"},{\"x\":1593705600000,\"y\":1.1843,\"equityReturn\":2.3419,\"unitMoney\":\"\"},{\"x\":1593964800000,\"y\":1.2481,\"equityReturn\":5.3871,\"unitMoney\":\"\"},{\"x\":1594051200000,\"y\":1.2518,\"equityReturn\":0.2965,\"unitMoney\":\"\"},{\"x\":1594137600000,\"y\":1.2758,\"equityReturn\":1.9172,\"unitMoney\":\"\"},{\"x\":1594224000000,\"y\":1.3014,\"equityReturn\":2.0066,\"unitMoney\":\"\"},{\"x\":1594310400000,\"y\":1.2643,\"equityReturn\":-2.8508,\"unitMoney\":\"\"}];/*累计净值走势*/var Data_ACWorthTrend = [[1513872000000,1.0],[1514390400000,1.0005],[1514476800000,1.0009],[1514649600000,1.0015],[1514822400000,1.0076],[1514908800000,1.0095],[1514995200000,1.0133],[1515081600000,1.0143],[1515340800000,1.0197],[1515427200000,1.0146],[1515513600000,1.0185],[1515600000000,1.015],[1515686400000,1.016],[1515945600000,1.0117],[1516032000000,1.0198],[1516118400000,1.021],[1516204800000,1.031],[1516291200000,1.0418],[1516550400000,1.0541],[1516636800000,1.0731],[1516723200000,1.0871],[1516809600000,1.0937],[1516896000000,1.0954],[1517155200000,1.0845],[1517241600000,1.0613],[1517328000000,1.0706],[1517414400000,1.0655],[1517500800000,1.0878],[1517760000000,1.1173],[1517846400000,1.0859],[1517932800000,1.0613],[1518019200000,1.0336],[1518105600000,0.9902],[1518364800000,1.0017],[1518451200000,1.011],[1518537600000,1.02],[1519228800000,1.035],[1519315200000,1.0394],[1519574400000,1.0537],[1519660800000,1.0336],[1519747200000,1.031],[1519833600000,1.0306],[1519920000000,1.0164],[1520179200000,1.0072],[1520265600000,1.0227],[1520352000000,1.0121],[1520438400000,1.014],[1520524800000,1.0094],[1520784000000,1.0173],[1520870400000,1.0128],[1520956800000,1.0119],[1521043200000,1.0095],[1521129600000,1.0032],[1521388800000,0.9942],[1521475200000,0.9972],[1521561600000,0.991],[1521648000000,0.9832],[1521734400000,0.9512],[1521993600000,0.9492],[1522080000000,0.9548],[1522166400000,0.9468],[1522252800000,0.9642],[1522339200000,0.9617],[1522598400000,0.9659],[1522684800000,0.9576],[1522771200000,0.9572],[1523203200000,0.9655],[1523289600000,0.9879],[1523376000000,0.9953],[1523462400000,0.9862],[1523548800000,0.9807],[1523808000000,0.959],[1523894400000,0.9552],[1523980800000,0.9711],[1524067200000,0.9818],[1524153600000,0.9674],[1524412800000,0.9633],[1524499200000,0.9865],[1524585600000,0.9807],[1524672000000,0.9692],[1524758400000,0.9756],[1525190400000,0.9739],[1525276800000,0.9776],[1525363200000,0.9724],[1525622400000,0.9833],[1525708800000,0.9903],[1525795200000,0.9841],[1525881600000,0.9835],[1525968000000,0.9845],[1526227200000,0.9826],[1526313600000,0.9819],[1526400000000,0.9722],[1526486400000,0.9711],[1526572800000,0.9768],[1526832000000,0.9779],[1526918400000,0.9715],[1527004800000,0.95009999999999994],[1527091200000,0.9478],[1527177600000,0.9399],[1527436800000,0.9368],[1527523200000,0.9344],[1527609600000,0.9168],[1527696000000,0.9278],[1527782400000,0.9311],[1528041600000,0.9353],[1528128000000,0.9372],[1528214400000,0.936],[1528300800000,0.9467],[1528387200000,0.9375],[1528646400000,0.94],[1528732800000,0.9491],[1528819200000,0.9435],[1528905600000,0.9484],[1528992000000,0.9458],[1529337600000,0.916],[1529424000000,0.9235],[1529510400000,0.9206],[1529596800000,0.9317],[1529856000000,0.9221],[1529942400000,0.9056],[1530028800000,0.8949],[1530115200000,0.8913],[1530201600000,0.9077],[1530288000000,0.9076],[1530460800000,0.8888],[1530547200000,0.8894],[1530633600000,0.8878],[1530720000000,0.8819],[1530806400000,0.8861],[1531065600000,0.9051],[1531152000000,0.9144],[1531238400000,0.9116],[1531324800000,0.9198],[1531411200000,0.9206],[1531670400000,0.916],[1531756800000,0.9082],[1531843200000,0.9058],[1531929600000,0.9142],[1532016000000,0.9332],[1532275200000,0.9493],[1532361600000,0.9609],[1532448000000,0.9614],[1532534400000,0.9509],[1532620800000,0.9551],[1532880000000,0.9597],[1532966400000,0.9613],[1533052800000,0.9448],[1533139200000,0.9452],[1533225600000,0.9441],[1533484800000,0.9411],[1533571200000,0.9636],[1533657600000,0.9586],[1533744000000,0.9642],[1533830400000,0.9602],[1534089600000,0.964],[1534176000000,0.9571],[1534262400000,0.9485],[1534348800000,0.9448],[1534435200000,0.9406],[1534694400000,0.9466],[1534780800000,0.952],[1534867200000,0.9495],[1534953600000,0.9477],[1535040000000,0.948],[1535299200000,0.9556],[1535385600000,0.9587],[1535472000000,0.9539],[1535558400000,0.941],[1535644800000,0.9372],[1535904000000,0.934],[1535990400000,0.9376],[1536076800000,0.9269],[1536163200000,0.9264],[1536249600000,0.9311],[1536508800000,0.9243],[1536595200000,0.9085],[1536681600000,0.9058],[1536768000000,0.9084],[1536854400000,0.9102],[1537113600000,0.9035],[1537200000000,0.9149],[1537286400000,0.923],[1537372800000,0.9234],[1537459200000,0.9351],[1537804800000,0.9303],[1537891200000,0.9345],[1537977600000,0.9311],[1538064000000,0.9355],[1538928000000,0.9214],[1539014400000,0.9231],[1539100800000,0.9304],[1539187200000,0.9101],[1539273600000,0.9202],[1539532800000,0.9126],[1539619200000,0.8999],[1539705600000,0.9076],[1539792000000,0.8862],[1539878400000,0.8943],[1540137600000,0.9085],[1540224000000,0.9005],[1540310400000,0.9018],[1540396800000,0.9013],[1540483200000,0.9042],[1540742400000,0.8956],[1540828800000,0.8971],[1540915200000,0.9019],[1541001600000,0.9005],[1541088000000,0.912],[1541347200000,0.9068],[1541433600000,0.9036],[1541520000000,0.8997],[1541606400000,0.8988],[1541692800000,0.8915],[1541952000000,0.8959],[1542038400000,0.8982],[1542124800000,0.8964],[1542211200000,0.9004],[1542297600000,0.9009],[1542556800000,0.9022],[1542643200000,0.8895],[1542729600000,0.8888],[1542816000000,0.8859],[1542902400000,0.8742],[1543161600000,0.8716],[1543248000000,0.8711],[1543334400000,0.8765],[1543420800000,0.8702],[1543507200000,0.8739],[1543766400000,0.8854],[1543852800000,0.8857],[1543939200000,0.8843],[1544025600000,0.8767],[1544112000000,0.8784],[1544371200000,0.8733],[1544457600000,0.8756],[1544544000000,0.8761],[1544630400000,0.8814],[1544716800000,0.8738],[1544976000000,0.8704],[1545062400000,0.8619],[1545148800000,0.8555],[1545235200000,0.8497],[1545321600000,0.8442],[1545580800000,0.8458],[1545667200000,0.8412],[1545753600000,0.8397],[1545840000000,0.8369],[1545926400000,0.8404],[1546185600000,0.8401],[1546358400000,0.8283],[1546444800000,0.8323],[1546531200000,0.8463],[1546790400000,0.8539],[1546876800000,0.8527],[1546963200000,0.8591],[1547049600000,0.8616],[1547136000000,0.864],[1547395200000,0.8634],[1547481600000,0.8689],[1547568000000,0.8676],[1547654400000,0.866],[1547740800000,0.8799],[1548000000000,0.8808],[1548086400000,0.8742],[1548172800000,0.8732],[1548259200000,0.8786],[1548345600000,0.8902],[1548604800000,0.8873],[1548691200000,0.8912],[1548777600000,0.8921],[1548864000000,0.893],[1548950400000,0.9044],[1549814400000,0.9107],[1549900800000,0.9136],[1549987200000,0.9253],[1550073600000,0.9269],[1550160000000,0.9167],[1550419200000,0.9358],[1550505600000,0.9327],[1550592000000,0.9314],[1550678400000,0.9309],[1550764800000,0.9456],[1551024000000,0.9881],[1551110400000,0.9738],[1551196800000,0.9706],[1551283200000,0.9694],[1551369600000,0.9841],[1551628800000,0.9958],[1551715200000,1.0078],[1551801600000,1.0157],[1551888000000,1.0136],[1551974400000,0.9774],[1552233600000,0.9973],[1552320000000,1.0086],[1552406400000,0.99],[1552492800000,0.9763],[1552579200000,0.985],[1552838400000,1.0099],[1552924800000,1.0139],[1553011200000,1.0113],[1553097600000,1.0161],[1553184000000,1.0123],[1553443200000,0.9953],[1553529600000,0.9757],[1553616000000,0.9799],[1553702400000,0.9743],[1553788800000,0.9967],[1554048000000,1.035],[1554134400000,1.0404],[1554220800000,1.0472],[1554307200000,1.059],[1554652800000,1.0665],[1554739200000,1.0494],[1554825600000,1.0413],[1554912000000,1.023],[1554998400000,1.0199],[1555257600000,1.0147],[1555344000000,1.0288],[1555430400000,1.0317],[1555516800000,1.0288],[1555603200000,1.0331],[1555862400000,1.0138],[1555948800000,1.0037],[1556035200000,1.0016],[1556121600000,0.9795],[1556208000000,0.9646],[1556467200000,0.9508],[1556553600000,0.9644],[1557072000000,0.9211],[1557158400000,0.9274],[1557244800000,0.9267],[1557331200000,0.9233],[1557417600000,0.9473],[1557676800000,0.9424],[1557763200000,0.9396],[1557849600000,0.9525],[1557936000000,0.9551],[1558022400000,0.9375],[1558281600000,0.93],[1558368000000,0.9372],[1558454400000,0.9365],[1558540800000,0.9258],[1558627200000,0.9206],[1558886400000,0.928],[1558972800000,0.9303],[1559059200000,0.9315],[1559145600000,0.9219],[1559232000000,0.9195],[1559491200000,0.9099],[1559577600000,0.8968],[1559664000000,0.8945],[1559750400000,0.8873],[1560096000000,0.8937],[1560182400000,0.9166],[1560268800000,0.9122],[1560355200000,0.909],[1560441600000,0.9007],[1560700800000,0.9001],[1560787200000,0.8998],[1560873600000,0.9097],[1560960000000,0.9305],[1561046400000,0.9387],[1561305600000,0.9343],[1561392000000,0.9279],[1561478400000,0.9287],[1561564800000,0.9364],[1561651200000,0.9309],[1561824000000,0.9308],[1561910400000,0.9474],[1561996800000,0.9495],[1562083200000,0.9442],[1562169600000,0.9437],[1562256000000,0.9441],[1562515200000,0.9271],[1562601600000,0.9268],[1562688000000,0.9241],[1562774400000,0.9222],[1562860800000,0.9273],[1563120000000,0.9333],[1563206400000,0.9337],[1563292800000,0.9367],[1563379200000,0.9282],[1563465600000,0.9332],[1563724800000,0.9233],[1563811200000,0.9266],[1563897600000,0.9316],[1563984000000,0.9384],[1564070400000,0.9446],[1564329600000,0.9456],[1564416000000,0.9493],[1564502400000,0.9471],[1564588800000,0.9445],[1564675200000,0.9368],[1564934400000,0.9282],[1565020800000,0.9201],[1565107200000,0.9231],[1565193600000,0.9269],[1565280000000,0.922],[1565539200000,0.9294],[1565625600000,0.9257],[1565712000000,0.9288],[1565798400000,0.9305],[1565884800000,0.9338],[1566144000000,0.9514],[1566230400000,0.952],[1566316800000,0.9533],[1566403200000,0.9523],[1566489600000,0.9527],[1566748800000,0.9447],[1566835200000,0.9535],[1566921600000,0.9531],[1567008000000,0.9539],[1567094400000,0.952],[1567353600000,0.9636],[1567440000000,0.9688],[1567526400000,0.974],[1567612800000,0.984],[1567699200000,0.9873],[1567958400000,1.0028],[1568044800000,1.0002],[1568131200000,0.9959],[1568217600000,0.9977],[1568563200000,0.9923],[1568649600000,0.9768],[1568736000000,0.9752],[1568822400000,0.9805],[1568908800000,0.9842],[1569168000000,0.9795],[1569254400000,0.9811],[1569340800000,0.9744],[1569427200000,0.967],[1569513600000,0.9684],[1569772800000,0.961],[1570464000000,0.9632],[1570550400000,0.9711],[1570636800000,0.9791],[1570723200000,0.9878],[1570982400000,1.0034],[1571068800000,0.9964],[1571155200000,0.9916],[1571241600000,0.9898],[1571328000000,0.9812],[1571587200000,0.9806],[1571673600000,0.9833],[1571760000000,0.9812],[1571846400000,0.9822],[1571932800000,0.9845],[1572192000000,0.9888],[1572278400000,0.9891],[1572364800000,0.986],[1572451200000,0.9795],[1572537600000,0.9871],[1572796800000,0.9923],[1572883200000,1.0044],[1572969600000,1.0017],[1573056000000,1.0042],[1573142400000,1.0015],[1573401600000,0.9917],[1573488000000,0.9909],[1573574400000,0.991],[1573660800000,0.9904],[1573747200000,0.9853],[1574006400000,0.9927],[1574092800000,1.002],[1574179200000,0.9979],[1574265600000,0.9945],[1574352000000,0.9886],[1574611200000,0.9921],[1574697600000,0.9919],[1574784000000,0.9896],[1574870400000,0.9849],[1574956800000,0.9822],[1575216000000,0.9825],[1575302400000,0.9841],[1575388800000,0.9839],[1575475200000,0.9898],[1575561600000,0.9942],[1575820800000,0.9971],[1575907200000,1.0005],[1575993600000,0.9998],[1576080000000,1.0034],[1576166400000,1.0136],[1576425600000,1.0232],[1576512000000,1.039],[1576598400000,1.0431],[1576684800000,1.0419],[1576771200000,1.0387],[1577030400000,1.0323],[1577116800000,1.0462],[1577203200000,1.0476],[1577289600000,1.0585],[1577376000000,1.0587],[1577635200000,1.0665],[1577721600000,1.0746],[1577894400000,1.0903],[1577980800000,1.0941],[1578240000000,1.0978],[1578326400000,1.1022],[1578412800000,1.0935],[1578499200000,1.0992],[1578585600000,1.0942],[1578844800000,1.1045],[1578931200000,1.1055],[1579017600000,1.0968],[1579104000000,1.0939],[1579190400000,1.0953],[1579449600000,1.1097],[1579536000000,1.0985],[1579622400000,1.1111],[1579708800000,1.0861],[1580659200000,1.0202],[1580745600000,1.0523],[1580832000000,1.0683],[1580918400000,1.0839],[1581004800000,1.0921],[1581264000000,1.0963],[1581350400000,1.0984],[1581436800000,1.1186],[1581523200000,1.1174],[1581609600000,1.1217],[1581868800000,1.151],[1581955200000,1.1659],[1582041600000,1.161],[1582128000000,1.1802],[1582214400000,1.1945],[1582473600000,1.2144],[1582560000000,1.2198],[1582646400000,1.1851],[1582732800000,1.1794],[1582819200000,1.1453],[1583078400000,1.1643],[1583164800000,1.1678],[1583251200000,1.1636],[1583337600000,1.1775],[1583424000000,1.1722],[1583683200000,1.1415],[1583769600000,1.1595],[1583856000000,1.1518],[1583942400000,1.137],[1584028800000,1.1314],[1584288000000,1.0975],[1584374400000,1.0916],[1584460800000,1.0843],[1584547200000,1.0794],[1584633600000,1.1008],[1584892800000,1.0707],[1584979200000,1.0818],[1585065600000,1.1029],[1585152000000,1.0949],[1585238400000,1.0877],[1585497600000,1.0773],[1585584000000,1.0775],[1585670400000,1.0772],[1585756800000,1.0825],[1585843200000,1.0748],[1586188800000,1.0947],[1586275200000,1.0944],[1586361600000,1.0989],[1586448000000,1.0824],[1586707200000,1.0735],[1586793600000,1.0833],[1586880000000,1.0786],[1586966400000,1.0822],[1587052800000,1.0867],[1587312000000,1.0935],[1587398400000,1.088],[1587484800000,1.096],[1587571200000,1.0971],[1587657600000,1.0884],[1587916800000,1.092],[1588003200000,1.0933],[1588089600000,1.0921],[1588176000000,1.1032],[1588694400000,1.1121],[1588780800000,1.1095],[1588867200000,1.1156],[1589126400000,1.1128],[1589212800000,1.1093],[1589299200000,1.1087],[1589385600000,1.094],[1589472000000,1.0958],[1589731200000,1.0857],[1589817600000,1.0867],[1589904000000,1.0731],[1589990400000,1.067],[1590076800000,1.0564],[1590336000000,1.0479],[1590422400000,1.0591],[1590508800000,1.0607],[1590595200000,1.0614],[1590681600000,1.0713],[1590940800000,1.0927],[1591027200000,1.0973],[1591113600000,1.0946],[1591200000000,1.0933],[1591286400000,1.0906],[1591545600000,1.0913],[1591632000000,1.092],[1591718400000,1.0869],[1591804800000,1.0802],[1591891200000,1.0808],[1592150400000,1.0769],[1592236800000,1.0921],[1592323200000,1.0916],[1592409600000,1.0963],[1592496000000,1.104],[1592755200000,1.103],[1592841600000,1.1031],[1592928000000,1.1043],[1593360000000,1.0961],[1593446400000,1.1067],[1593532800000,1.1235],[1593619200000,1.1572],[1593705600000,1.1843],[1593964800000,1.2481],[1594051200000,1.2518],[1594137600000,1.2758],[1594224000000,1.3014],[1594310400000,1.2643]];/*累计收益率走势*/var Data_grandTotal = [{\"name\":\"华商可转债债券C\",\"data\":[[1578585600000,0],[1578844800000,0.94],[1578931200000,1.03],[1579017600000,0.24],[1579104000000,-0.03],[1579190400000,0.10],[1579449600000,1.42],[1579536000000,0.39],[1579622400000,1.54],[1579708800000,-0.74],[1580659200000,-6.76],[1580745600000,-3.83],[1580832000000,-2.37],[1580918400000,-0.94],[1581004800000,-0.19],[1581264000000,0.19],[1581350400000,0.38],[1581436800000,2.23],[1581523200000,2.12],[1581609600000,2.51],[1581868800000,5.19],[1581955200000,6.55],[1582041600000,6.10],[1582128000000,7.86],[1582214400000,9.17],[1582473600000,10.99],[1582560000000,11.48],[1582646400000,8.31],[1582732800000,7.79],[1582819200000,4.67],[1583078400000,6.41],[1583164800000,6.73],[1583251200000,6.34],[1583337600000,7.61],[1583424000000,7.13],[1583683200000,4.32],[1583769600000,5.97],[1583856000000,5.26],[1583942400000,3.91],[1584028800000,3.40],[1584288000000,0.30],[1584374400000,-0.24],[1584460800000,-0.90],[1584547200000,-1.35],[1584633600000,0.60],[1584892800000,-2.15],[1584979200000,-1.13],[1585065600000,0.80],[1585152000000,0.06],[1585238400000,-0.59],[1585497600000,-1.54],[1585584000000,-1.53],[1585670400000,-1.55],[1585756800000,-1.07],[1585843200000,-1.77],[1586188800000,0.05],[1586275200000,0.02],[1586361600000,0.43],[1586448000000,-1.08],[1586707200000,-1.89],[1586793600000,-1.00],[1586880000000,-1.43],[1586966400000,-1.10],[1587052800000,-0.69],[1587312000000,-0.06],[1587398400000,-0.57],[1587484800000,0.16],[1587571200000,0.27],[1587657600000,-0.53],[1587916800000,-0.20],[1588003200000,-0.08],[1588089600000,-0.19],[1588176000000,0.82],[1588694400000,1.64],[1588780800000,1.40],[1588867200000,1.96],[1589126400000,1.70],[1589212800000,1.38],[1589299200000,1.33],[1589385600000,-0.02],[1589472000000,0.15],[1589731200000,-0.78],[1589817600000,-0.69],[1589904000000,-1.93],[1589990400000,-2.49],[1590076800000,-3.45],[1590336000000,-4.23],[1590422400000,-3.21],[1590508800000,-3.06],[1590595200000,-3.00],[1590681600000,-2.09],[1590940800000,-0.14],[1591027200000,0.28],[1591113600000,0.04],[1591200000000,-0.08],[1591286400000,-0.33],[1591545600000,-0.27],[1591632000000,-0.20],[1591718400000,-0.67],[1591804800000,-1.28],[1591891200000,-1.22],[1592150400000,-1.58],[1592236800000,-0.19],[1592323200000,-0.24],[1592409600000,0.19],[1592496000000,0.90],[1592755200000,0.80],[1592841600000,0.81],[1592928000000,0.92],[1593360000000,0.17],[1593446400000,1.14],[1593532800000,2.68],[1593619200000,5.76],[1593705600000,8.23],[1593964800000,14.07],[1594051200000,14.40],[1594137600000,16.60],[1594224000000,18.94],[1594310400000,15.55]]},{\"name\":\"同类平均\",\"data\":[[1578585600000,0],[1578844800000,0.11],[1578931200000,0.10],[1579017600000,0.05],[1579104000000,0.03],[1579190400000,0.06],[1579449600000,0.23],[1579536000000,0.19],[1579622400000,0.29],[1579708800000,0.08],[1580659200000,-0.16],[1580745600000,0.12],[1580832000000,0.25],[1580918400000,0.44],[1581004800000,0.51],[1581264000000,0.66],[1581350400000,0.70],[1581436800000,0.89],[1581523200000,0.89],[1581609600000,0.91],[1581868800000,1.13],[1581955200000,1.21],[1582041600000,1.18],[1582128000000,1.33],[1582214400000,1.44],[1582473600000,1.58],[1582560000000,1.60],[1582646400000,1.42],[1582732800000,1.46],[1582819200000,1.20],[1583078400000,1.39],[1583164800000,1.45],[1583251200000,1.51],[1583337600000,1.67],[1583424000000,1.68],[1583683200000,1.55],[1583769600000,1.63],[1583856000000,1.54],[1583942400000,1.40],[1584028800000,1.26],[1584288000000,0.95],[1584374400000,0.87],[1584460800000,0.82],[1584547200000,0.80],[1584633600000,1.02],[1584892800000,0.83],[1584979200000,1.00],[1585065600000,1.24],[1585152000000,1.25],[1585238400000,1.23],[1585497600000,1.16],[1585584000000,1.22],[1585670400000,1.28],[1585756800000,1.39],[1585843200000,1.36],[1586188800000,1.89],[1586275200000,2.04],[1586361600000,2.11],[1586448000000,1.97],[1586707200000,1.87],[1586793600000,2.01],[1586880000000,2.02],[1586966400000,2.13],[1587052800000,2.19],[1587312000000,2.25],[1587398400000,2.24],[1587484800000,2.35],[1587571200000,2.41],[1587657600000,2.37],[1587916800000,2.39],[1588003200000,2.42],[1588089600000,2.45],[1588176000000,2.56],[1588694400000,2.59],[1588780800000,2.44],[1588867200000,2.46],[1589126400000,2.30],[1589212800000,2.26],[1589299200000,2.26],[1589385600000,2.16],[1589472000000,2.19],[1589731200000,2.08],[1589817600000,2.09],[1589904000000,1.96],[1589990400000,1.90],[1590076800000,1.86],[1590336000000,1.75],[1590422400000,1.74],[1590508800000,1.62],[1590595200000,1.62],[1590681600000,1.72],[1590940800000,1.90],[1591027200000,1.68],[1591113600000,1.49],[1591200000000,1.38],[1591286400000,1.24],[1591545600000,1.23],[1591632000000,1.31],[1591718400000,1.32],[1591804800000,1.32],[1591891200000,1.41],[1592150400000,1.43],[1592236800000,1.47],[1592323200000,1.42],[1592409600000,1.47],[1592496000000,1.51],[1592755200000,1.43],[1592841600000,1.36],[1592928000000,1.45],[1593360000000,1.46],[1593446400000,1.61],[1593532800000,1.73],[1593619200000,1.94],[1593705600000,2.08],[1593964800000,2.37],[1594051200000,2.31],[1594137600000,2.39],[1594224000000,2.41],[1594310400000,2.24]]},{\"name\":\"沪深300\",\"data\":[[1578585600000,0],[1578844800000,0.98],[1578931200000,0.64],[1579017600000,0.09],[1579104000000,-0.34],[1579190400000,-0.20],[1579449600000,0.54],[1579536000000,-1.17],[1579622400000,-0.75],[1579708800000,-3.83],[1580659200000,-11.41],[1580745600000,-9.07],[1580832000000,-8.04],[1580918400000,-6.33],[1581004800000,-6.32],[1581264000000,-5.94],[1581350400000,-5.06],[1581436800000,-4.29],[1581523200000,-4.88],[1581609600000,-4.21],[1581868800000,-2.06],[1581955200000,-2.54],[1582041600000,-2.69],[1582128000000,-0.45],[1582214400000,-0.33],[1582473600000,-0.73],[1582560000000,-0.94],[1582646400000,-2.17],[1582732800000,-1.88],[1582819200000,-5.36],[1583078400000,-2.25],[1583164800000,-1.73],[1583251200000,-1.16],[1583337600000,1.05],[1583424000000,-0.59],[1583683200000,-3.99],[1583769600000,-1.93],[1583856000000,-3.24],[1583942400000,-5.10],[1584028800000,-6.43],[1584288000000,-10.46],[1584374400000,-10.89],[1584460800000,-12.66],[1584547200000,-13.79],[1584633600000,-12.25],[1584892800000,-15.20],[1584979200000,-12.92],[1585065600000,-10.58],[1585152000000,-11.17],[1585238400000,-10.88],[1585497600000,-11.75],[1585584000000,-11.46],[1585670400000,-11.72],[1585756800000,-10.30],[1585843200000,-10.81],[1586188800000,-8.77],[1586275200000,-9.20],[1586361600000,-8.90],[1586448000000,-9.46],[1586707200000,-9.85],[1586793600000,-8.11],[1586880000000,-8.79],[1586966400000,-8.67],[1587052800000,-7.78],[1587312000000,-7.44],[1587398400000,-8.53],[1587484800000,-7.78],[1587571200000,-8.01],[1587657600000,-8.80],[1587916800000,-8.18],[1588003200000,-7.54],[1588089600000,-7.11],[1588176000000,-6.02],[1588694400000,-5.45],[1588780800000,-5.72],[1588867200000,-4.79],[1589126400000,-4.88],[1589212800000,-4.87],[1589299200000,-4.68],[1589385600000,-5.72],[1589472000000,-6.01],[1589731200000,-5.77],[1589817600000,-4.97],[1589904000000,-5.48],[1589990400000,-5.99],[1590076800000,-8.15],[1590336000000,-8.02],[1590422400000,-6.98],[1590508800000,-7.63],[1590595200000,-7.36],[1590681600000,-7.11],[1590940800000,-4.61],[1591027200000,-4.31],[1591113600000,-4.31],[1591200000000,-4.35],[1591286400000,-3.89],[1591545600000,-3.39],[1591632000000,-2.79],[1591718400000,-2.97],[1591804800000,-4.02],[1591891200000,-3.85],[1592150400000,-5.00],[1592236800000,-3.57],[1592323200000,-3.50],[1592409600000,-2.85],[1592496000000,-1.55],[1592755200000,-1.47],[1592841600000,-0.99],[1592928000000,-0.58],[1593360000000,-1.28],[1593446400000,0.02],[1593532800000,2.03],[1593619200000,4.15],[1593705600000,6.16],[1593964800000,12.18],[1594051200000,12.85],[1594137600000,14.67],[1594224000000,16.28],[1594310400000,14.17]]}];;";
        Pattern dataNetWorth = Pattern.compile("var Data_netWorthTrend = ([^;]*);");
        Pattern dataAcNetWorth = Pattern.compile("var Data_ACWorthTrend = ([^;]*);");
        String dataNetWorthJSON = null, dataAcWorthJSON = null;
        Matcher matcherResult = dataNetWorth.matcher(resultStr);
        if (matcherResult.find()) {
            dataNetWorthJSON = matcherResult.group(1);
        } else {
            log.error("not found Data_netWorthTrend");
        }
        matcherResult = dataAcNetWorth.matcher(resultStr);
        if (matcherResult.find()) {
            dataAcWorthJSON = matcherResult.group(1);
        } else {
            log.error("not fund Data_ACWorthTrend");
        }
        log.info("netWorth: {} acWorth: {}", dataNetWorthJSON, dataAcWorthJSON);
        if (dataAcWorthJSON != null) {
            JSONArray jsonArray = JSON.parseArray(dataAcWorthJSON);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray resultArray = jsonArray.getJSONArray(i);
                Long timestamp = resultArray.getLong(0);
                BigDecimal value = resultArray.getBigDecimal(1);
//                log.info("timestamp: {} value: {}", timestamp, value);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                log.info("date: {} value: {}",
                        formatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeConstants.CHINA_ZONE)),
                        new BigDecimal(resultArray.getString(1)));
            }
        }
    }

    @Test
    public void testMul() {
        log.info("date: {}", DateUtil.formatDay(LocalDateTime.ofInstant(Instant.ofEpochMilli(1594828800000L), TimeConstants.CHINA_ZONE)));
    }

//    @Test

    /**
     * AES ECB 解密
     *
     * @param messageBase64 密文，base64编码
     * @param key           密匙，和加密时相同
     * @return 解密后数据
     */
    public static String decryptECB(byte[] messageByte, String key) {
        final String cipherMode = "AES/ECB/PKCS5Padding";
        final String charsetName = "UTF-8";
        try {

            //
            byte[] keyByte = key.getBytes(charsetName);
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");

            Cipher cipher = Cipher.getInstance(cipherMode);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] content = cipher.doFinal(messageByte);
            String result = new String(content, charsetName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Test
    public void testDecrypt() throws Exception {
        File file = new File("/Users/jiangwenjie/Downloads/main_1.bin");
        FileInputStream in = new FileInputStream(file);
        int l = -1;
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((l = in.read(buffer)) > 0) {
            bout.write(buffer, 0, l);
        }
        byte[] encrypts = bout.toByteArray();
        System.out.println(decryptECB(encrypts, "你挺能赖的啊撒逼"));
    }


    @Test
    public void replaceCodes() throws Exception {
        String[] variables = new String[]{
                "waitFor",
                "setMode",
                "normal",
                "height",
                "width",
                "write",
                "/sdcard/.系统文件请勿删除.txt",
                "create",
                "默认配置",
                "get",
                "put",
                "remove",
                "刷分配置",
                "账号位置",
                "singleChoice",
                "请选择刷分版本",
                "个人版",
                "自动换号版",
                "未选择版本已退出程序",
                "getAndroidId",
                "digest",
                "MD5",
                "未注册",
                "\n主板:",
                "board",
                "\n制造商:",
                "brand",
                "\n型号:",
                "model",
                "\n产品名称:",
                "product",
                "\n硬件名称:",
                "hardware",
                "\nAndroidId: ",
                ".txt",
                "/sdcard/.",
                "https://yhsc.oss-cn-beijing.aliyuncs.com",
                "用户账号/注册/封号：",
                "postMultipart",
                "statusCode",
                "已自动复制用户名至剪贴板，在输入框内长按粘贴即可查看。",
                "封号：",
                "用户账号/注册/",
                "注册成功！",
                "注册失败！",
                "http://yhsc.oss-cn-beijing.aliyuncs.com",
                "/用户账号/注册/",
                "statusMessage",
                "网络连接失败!",
                "用户名已存在!",
                "body",
                "string",
                "split",
                "成功登录",
                "您的用户名：\n",
                "slice",
                "您的到期时间：",
                "build",
                "继续(",
                "positive",
                "show",
                "setActionButton",
                "dismiss",
                "已到期!您的到期时间：",
                "已到期!您的到期时间：\n",
                "\n已自动复制用户名至剪贴板，在输入框内长按粘贴即可查看。",
                "已登录",
                "exists",
                "read",
                "/用户账号/注册/封号：",
                "检测到此手机存在恶意修改设备信息，如未将设备信息还原至注册时的设备信息将永久封停，详细信息请联系管理员!",
                "检测到此手机未注册本软件，将自动注册！\nPS:如已注册且弹出此对话框，可能是因为手机系统升级或卸载重装本软件导致，请及时联系管理员！",
                "未注册!\n正在自动注册...",
                "splice",
                "length",
                "网络错误!同步失败！",
                "一机一号请勿多开！\n请用原注册手机登录!",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "charAt",
                "floor",
                "random",
                "log",
                "text",
                "SimpleDateFormat",
                "format",
                "http://quan.suning.com/getSysTime.do",
                "json",
                "sysTime1",
                "网络错误",
                "open",
                "./题库.db",
                "execSQL",
                "CREATE TABLE IF NOT EXISTS TK(",
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, ",
                "`题目` TEXT NOT NULL, ",
                "`答案` TEXT NOT NULL ",
                "multiChoice",
                "请选择刷分配置",
                "评论、分享、收藏、本地",
                "文章视频",
                "每日答题",
                "每周答题",
                "专项答题",
                "挑战答题",
                "离线答题模式",
                "一键59分",
                "文章-视频 条数（不含时长）",
                "已开启离线模式！",
                "已开启条数模式！",
                "恭喜您，当前账号积分任务已完成！",
                "运行异常：",
                "/sdcard/账号密码.txt",
                "readlines",
                "总共读取到",
                "条账号及密码",
                "请到SD卡根目录：\n      账号密码.txt\n添加账号文件\n格式为每行一条账号信息：账号----密码\n账号----密码",
                "未选择,已结束程序！",
                "rawInput",
                "请输入从第几个账号开始刷分,读取到的总数量为：",
                "\n默认为1",
                "未选择账号已退出程序",
                "请输入指定时间运行(PS:24小时制6位数字)：\n如晚上8点20分01秒就输入：202001\n早上8点20分01秒就输入：082001\n不输入时间直接点确定为立即执行！",
                "yyyyMMddHHmmss",
                "现在是",
                "开始执行",
                "输入为空立即执行",
                "已选择从第",
                "个账号开始",
                "----",
                "******",
                "test",
                "开始登录第",
                "个账号",
                "您的输入有误，请核对后再次输入！",
                "账号密码正确！开始执行刷分程序！",
                "当前账号刷分结束,恭喜你积分已完成!",
                "跳过当前账号，不执行刷分程序！",
                "学习强国",
                "启动失败\n找不到该应用:学习强国。如有不懂请加群问群友。",
                "启动失败\n找不到该应用:学习强国",
                "comm_head_xuexi_mine",
                "findOne",
                "click",
                "学习积分",
                "my_setting",
                "android.widget.TextView",
                "退出登录",
                "账号已退出",
                "恭喜，你已登录",
                "btn_next",
                "et_phone_input",
                "setText",
                "et_pwd_login",
                "已尝试登录指定账号",
                "android.view.View",
                "提示条款：",
                "同意并继续",
                "common_webview",
                "bounds",
                "parent",
                "child",
                "right",
                "bottom",
                "暂不开启",
                "检测到：开启地理位置访问权限\n已拒绝开启",
                "home_bottom_tab_text",
                "普通成员的密码需6~20位字符；为保证安全性，管理员的密码需8~20位字符，请在大写英文字母，小写英文字母，数字和特殊符号中选择至少三项组合为密码。",
                "账号或密码错误!\n用户名为：",
                "号码或密码错误，请重新输入",
                "输入的手机号码无效，请确认后重试！",
                "手机号错误!\n用户名为：",
                "发表观点",
                "正在寻找",
                "find",
                "去看看",
                "。发表观点今日未得分!正在获取中...",
                "已完成",
                "发表观点今日已完成啦！",
                "出现未知错误!",
                "加强纪律性，革命无不胜。",
                "我们中华民族有同自己的敌人血战到底的气概，有在自力更生的基础上光复旧物的决心，有自立于世界民族之林的能力。",
                "革命总是会有牺牲的，我们是幸存者，只要有一口气，就要为党工作。",
                "中夜四五叹，常为大国忧。",
                "一个政党要引导革命到胜利，必须依靠自己政治路线的正确和组织上的巩固。",
                "天下兴亡，匹夫有责。",
                "忠诚印寸心，浩然充两间。",
                "中华民族不但以刻苦耐劳著称于世，同时又是酷爱自由、富于革命传统的民族。",
                "生活太安逸了，工作就会被生活所累。",
                "位卑未敢忘忧国。",
                "开始获取以下内容得分：\n评论、收藏、发表观点、分享",
                "textContains",
                "centerX",
                "centerY",
                "已找到文章，开始刷获取得分",
                "欢迎发表你的观点",
                "未报错",
                "启用备用兼容性点击",
                "view_pager",
                "点击坐标为X:",
                "学习才能强国",
                "findOnce",
                "android.widget.Button",
                "查看我的收藏",
                "我知道了",
                "indexInParent",
                "分享到学习强国",
                "选择联系人",
                "本地频道",
                "。本地频道今日未得分!正在获取中...",
                "本地频道今日已完成啦！",
                "开始获取以下内容得分：\n本地频道分数",
                "新思想",
                "textEndsWith",
                "学习平台",
                "检测到当前本地学习平台为：",
                "开始订阅",
                "订阅今日已完成啦！",
                "my_subscribe_tv",
                "我的订阅",
                "暂无相关内容",
                "你已经看到我的底线了",
                "android.widget.FrameLayout",
                "已返回顶层",
                "已订阅列表：",
                "强国号",
                "人民日报",
                "未订阅",
                "clickable",
                "订阅了",
                "push",
                "订阅已完成",
                "加载失败，点击重试",
                "加载中",
                "match",
                "文章学习时长",
                "阅读文章",
                "视听学习",
                "视听学习时长",
                "阅读文章今日未得分!正在获取中...",
                "视听学习今日未得分!正在获取中...",
                "今日基础得分已完成啦！",
                "去学习",
                "文章学习时长今日未得分!正在获取中...",
                "视听学习时长今日未得分!正在获取中...",
                "开始学习文章时长及条数大约需学习6次",
                "听新闻广播",
                "经典音乐广播",
                "btn_back",
                "dialog_cancel",
                "home_bottom_tab_button_ding",
                "正在检测温馨提示弹窗，预计3秒！",
                "温馨提示",
                "tv_close",
                "温馨提示已关闭",
                "正在等待刷新",
                "textMatches",
                "et_sendmessage",
                "识别到的控件位置异常，修改为点击屏幕中心坐标，如已看见此项提示后未正确进入百灵视频，则有可能是系统问题导致的点击失效，或者点击到了标题未能正确进入百灵视频。",
                "向上滑动切换内容",
                "\"向上滑动切换内容\"提示已关闭",
                "向右滑动退出浮层",
                "\"向右滑动退出浮层\"提示已关闭",
                "开始观看视频条数：第",
                "继续播放",
                "视频条数观看完毕",
                "开始阅读文章",
                "android.widget.SeekBar",
                "内容已下线",
                "此文章是视频或内容已下线，已自动跳过学习！",
                "开始学习文章时长及条数",
                "学习结束",
                "已学习",
                "开始每日答题",
                "去答题",
                "每日答题今日未得分!正在获取中...",
                "查看提示",
                "未识别到内容！",
                "每日答题今日已完成啦！",
                "出现未知错误！",
                "多选题 (10分)",
                "填空题 (10分)",
                "单选题 (10分)",
                "rawQuery",
                "SELECT * FROM TK WHERE 题目 = ?",
                "single",
                "A.|B.|C.|",
                "填空题",
                "indexOf",
                "A.|",
                "android.widget.RadioButton",
                "B.|",
                "C.|",
                "D.|",
                "题!\n随机延迟：",
                "未在答题界面",
                "未识别到题目",
                "未选择",
                "|《史记》",
                "|《清史》",
                "分解后答案：",
                "《左传》",
                "识别到答案为：《左传》，已选择",
                "已选择",
                "《清史》",
                "识别到答案为：《清史》，已选择",
                "[\\u4E00-\\u9FFF]+",
                "题库没有此题",
                "已启用离线答题模式",
                "textStartsWith",
                "正确答案：",
                "下一题",
                "/sdcard/题库未出现题目.txt",
                "writeline",
                "---",
                "close",
                "保存成功",
                "答案为空!无法保存",
                "android.webkit.WebView",
                "top",
                "正确答案： ",
                "join",
                "无法识别到答案！请手动记录!",
                "退出答题界面！",
                "进入学习强国界面",
                "开始每周答题",
                "每周答题今日未得分!正在获取中...",
                "加载中...",
                "网络开小差",
                "您已经看到了我的底线",
                "未作答",
                "已找到未完成题目开始答题！",
                "每周答题已经做完啦！",
                "每周答题今日已完成啦！",
                "出现未知错误",
                "A.|B.|",
                "第一题识别到题库没有此项答案，每周答题可能为最新出的题目，题库没有录入此题，将退出答题界面跳过此项得分。请及时到群内@管理员更新题库后下次运行软件将会再次识别。",
                "/sdcard/未出现题目及答案备份.txt",
                "每周答题:\r",
                "/sdcard/题库.txt",
                "正在保存：",
                "开始专项答题",
                "专项答题今日未得分!正在获取中...",
                "已过期",
                "重新答题",
                "继续答题",
                "开始答题",
                "专项答题已经做完啦！",
                "专项答题今日已完成啦！",
                "多选题(10分)回答错误",
                "填空题(10分)回答错误",
                "单选题(10分)回答错误",
                "第一题识别到题库没有此项答案，专项答题可能为最新出的题目，题库没有录入此题，将退出答题界面跳过此项得分。请及时到群内@管理员更新题库后下次运行软件将会再次识别。",
                "回答正确",
                "本次作答分数",
                "多选题 (10分)回答错误",
                "填空题 (10分)回答错误",
                "单选题 (10分)回答错误",
                "多选题 (10分)回答正确",
                "填空题 (10分)回答正确",
                "单选题 (10分)回答正确",
                "专项答题:\r",
                "开始挑战答题",
                "今日已",
                "挑战答题今日未得分!正在获取中...",
                "挑战答题今日已完成啦！",
                "childCount",
                "答题结束!\n请等待计时结束！",
                "请稍后到sd卡根目录\"题库未出现题目.txt\"文件中查看",
                "题库识别中",
                "出现异常，答题结束!",
                "/sdcard/挑战答题题库未出现题目.txt",
                "多选题",
                "单选题",
                "选择题",
                "android.widget.EditText",
                "([^/s]*?)",
                "题目类型不支持查看答案.",
                "input",
                "查看提示内未发现相同题目,已随机填写答案！",
                "查看提示内未发现相同题目,已随机填写答案",
                "剩余时间",
                "返回上一题",
                "检测到登录界面，个人版请登录后运行！软件已暂停运行30秒请及时登录。",
                "start",
                "点击重试",
                "检测到了内容已下线，自动返回主页面"
        };
        File mixed = new File("/Users/jiangwenjie/Documents/Repositories/Github/Ant-Forest-Webpack/src/main_mixed.js");
        FileInputStream fileInputStream = new FileInputStream(mixed);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        String readLine = null;
        StringBuilder stringBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile("_0x422b\\('0x([\\da-f]+)'\\)");
        while ((readLine = br.readLine()) != null) {
            Matcher matcher = pattern.matcher(readLine);
            while (matcher.find()) {
                String indexHex = matcher.group(1);
                int realIdx = Integer.valueOf(indexHex, 16);
                String realVal = variables[realIdx];
                readLine = readLine.replaceFirst(pattern.pattern(), "'" + realVal + "'");
                matcher = pattern.matcher(readLine);
            }
            stringBuilder.append(readLine.replaceAll("\\n", "\\n")).append("\n");

        }
        System.out.println(stringBuilder.toString());
        fileInputStream.close();
    }

    @Test
    public void testHexToInt() {
        System.out.println(Integer.parseInt("9a", 16));
    }
}
