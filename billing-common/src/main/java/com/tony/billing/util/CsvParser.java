package com.tony.billing.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/1.
 */
public class CsvParser {
    private BufferedReader bufferedReader;
    private List<String> cvsLines;

    {
        init();
    }

    public CsvParser(String filePath) {
        InputStreamReader inputStreamReader = null;
        try {
            InputStream inputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            readInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CsvParser(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            readInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        cvsLines = new ArrayList<String>();
    }

    private void readInformation() throws IOException {
        String readStr = null;
        while ((readStr = bufferedReader.readLine()) != null) {
            cvsLines.add(readStr);
        }
    }

    public List<String> getList() {
        return cvsLines;
    }

    public List<String> getListWithNoHeader() {
        return cvsLines.subList(2, cvsLines.size());
    }

    public List<String> getListCustom(Integer start, Integer end) {
        return cvsLines.subList(start, end == null ? cvsLines.size() : end);
    }

    /**
     * 得到csv文件的行数
     */

    public int getRowNum() {
        return cvsLines.size();
    }

    /**
     * 得到csv文件的列数
     */
    public int getColNum() {
        if (!"[]".equals(cvsLines.toString())) {
            // csv文件中，每列之间的是用','来分隔的
            if (cvsLines.get(0).contains(",")) {
                return cvsLines.get(0).split(",").length;
            } else if (cvsLines.get(0).trim().length() != 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 取得指定行的值
     *
     * @param index
     * @return
     */
    public String getRow(int index) {
        if (this.cvsLines.size() != 0) {
            return cvsLines.get(index);
        } else {
            return null;
        }
    }

    /**
     * 取得指定列的值
     */
    public String getCol(int index) {
        if (this.getColNum() == 0) {
            return null;
        }
        StringBuffer scol = new StringBuffer();
        String temp = null;
        int colnum = this.getColNum();
        if (colnum > 1) {
            for (String cvsLine : cvsLines) {
                temp = cvsLine;
                scol = scol.append(temp.split(",")[index]).append(",");
            }
        } else {
            for (String cvsLine : cvsLines) {
                temp = cvsLine;
                scol = scol.append(temp).append(",");
            }
        }
        String str = scol.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 取得指定行，指定列的值
     *
     * @param row
     * @param col
     * @return
     */
    public String getString(int row, int col) {
        String temp = null;
        int colnum = this.getColNum();
        if (colnum > 1) {
            temp = cvsLines.get(row).split(",")[col];
        } else if (colnum == 1) {
            temp = cvsLines.get(row);
        } else {
            temp = null;
        }
        return temp;
    }

    public void csvClose() throws IOException {
        this.bufferedReader.close();
    }

    public List readCvs(String filename) throws IOException {
        CsvParser cu = new CsvParser(new FileInputStream(new File(filename)));
        List cvsLines = cu.getList();

        return cvsLines;
    }

    public void createCsv(String biao, List cvsLines, String path)
            throws IOException {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String dateToday = dataFormat.format(today);
        File file = new File(path + "resource/expert/" + dateToday
                + "importerrorinfo.csv");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
        }
        StringBuilder sb = new StringBuilder("");
        sb.append(biao);
        FileOutputStream writerStream = new FileOutputStream(file, true);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                writerStream, "UTF-8"));
        for (Object aTt : cvsLines) {
            String fileStr = aTt.toString();

            sb.append(fileStr).append("\r\n");
        }
        output.write(sb.toString());
        output.flush();
        output.close();
    }


    static class Record {
        private String tradeNo;
        private String orderNo;
        private String createTime;
        private String paidTime;
        private String modifyTime;
        private String location;
        private String orderType;
        private String target;
        private String goodsName;
        private String money;
        private String inOutType;
        private String orderStatus;
        private String serviceCost;
        private String refundMoney;
        private String memo;
        private String tradeStatus;

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPaidTime() {
            return paidTime;
        }

        public void setPaidTime(String paidTime) {
            this.paidTime = paidTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getInOutType() {
            return inOutType;
        }

        public void setInOutType(String inOutType) {
            this.inOutType = inOutType;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getServiceCost() {
            return serviceCost;
        }

        public void setServiceCost(String serviceCost) {
            this.serviceCost = serviceCost;
        }

        public String getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(String refundMoney) {
            this.refundMoney = refundMoney;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }
    }

    private static void generatorCreateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE t_cost_info (\n");
        sb.append("id int not null primary key,\n");
        Class clz = Record.class;
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getType().getName());
            if (field.getType().getName().equals(String.class.getName())) {
                sb.append(field.getName()).append(" varchar(100)").append(" null,\n");
            } else {
                sb.append(field.getName()).append(" varchar(100)").append(" null,\n");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        System.out.println(sb.toString());
    }

    private static void generatorInsertSQL(Record record) throws InvocationTargetException, IllegalAccessException {
        Class clz = Record.class;
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO t_cost_info (");
        Field[] fields = clz.getDeclaredFields();
        Method[] methods = clz.getMethods();
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }
        String methodName;
        Method method;
        List<String> notMethod = new ArrayList<String>();
        Map<String, Object> notNull = new HashMap<String, Object>();
        for (Field field : fields) {
            methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            method = methodMap.get(methodName);
            Object result = null;
            try {
                result = method.invoke(record);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (result != null) {
                notMethod.add(method.getName());
                notNull.put(method.getName(), result);
                sb.append(field.getName()).append(",");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(") VALUES (");
        for (String methodN : notMethod) {

            if (methodN.contains("Money") || methodN.contains("Cost")) {
                sb.append("'").append(yuan2fen(notNull.get(methodN).toString())).append("',");
            } else {
                sb.append("'").append(notNull.get(methodN).toString()).append("',");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        System.out.println(sb.toString());
    }


    private static Long yuan2fen(String money) {
        if (StringUtils.isEmpty(money)) {
            return null;
        }
        return Long.valueOf(money.replace(".", ""));
    }

}
