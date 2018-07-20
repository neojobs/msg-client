package com.hexun.msg.client.remoting.proto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.hexun.msg.client.common.MQVersion;
import com.hexun.msg.client.remoting.codec.RemotingSerializable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by neo on 2018/7/20.
 */
public class RemotingCommand {
    private static final int RPC_TYPE = 0;
    private static final AtomicInteger requestId = new AtomicInteger();
    private int code;
    private LanguageCode language = LanguageCode.JAVA;
    private int version = 0;
    private int opaque = requestId.getAndIncrement();
    private int flag = 0;
    private String remark;

    private HashMap<String, String> extFields;
    private transient CommandCustomHeader customHeader;

    private SerializeType serializeTypeCurrentRPC = SerializeType.JSON;

    private transient byte[] body;


    @JSONField(serialize = false)
    public RemotingCommandType getCmdType() {
        int type = flag & 1;
        if(type == 0) {
            return RemotingCommandType.REQUEST_COMMAND;
        }
        return RemotingCommandType.RESPONSE_COMMAND;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LanguageCode getLanguage() {
        return language;
    }

    public void setLanguage(LanguageCode language) {
        this.language = language;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOpaque() {
        return opaque;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public HashMap<String, String> getExtFields() {
        return extFields;
    }

    public void setExtFields(HashMap<String, String> extFields) {
        this.extFields = extFields;
    }

    public CommandCustomHeader getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(CommandCustomHeader customHeader) {
        this.customHeader = customHeader;
    }

    public SerializeType getSerializeTypeCurrentRPC() {
        return serializeTypeCurrentRPC;
    }

    public void setSerializeTypeCurrentRPC(SerializeType serializeTypeCurrentRPC) {
        this.serializeTypeCurrentRPC = serializeTypeCurrentRPC;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public static RemotingCommand createRequest(int code, CommandCustomHeader customHeader) {
        RemotingCommand command = new RemotingCommand();
        command.setCode(code);
        command.setVersion(MQVersion.CURRENT_VERSION);
        command.setCustomHeader(customHeader);
        return command;
    }

    @JSONField(serialize = false)
    public int getBodyLength() {
        if(body == null) {
            return 0;
        }
        return body.length;
    }

    public ByteBuf encode() {
        byte []headerData = encodeHeader();
        int headerLength = 0;
        if(headerData != null) {
            headerLength = headerData.length;
        }
        int length = headerLength + getBodyLength() + 4;
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(length);
        buf.writeInt(length);
        writeHeaderLength(buf, headerLength);
        buf.writeBytes(headerData);

        if(body != null) {
            buf.writeBytes(body);
        }
        return buf;
    }

    private byte[] encodeHeader() {
        if(extFields == null) {
            extFields = new HashMap<String, String>();
        }
        if(customHeader != null) {
            makeCustomHeaderToNet();
        }
        byte []headerData = RemotingSerializable.serialize(this);
        return  headerData;
    }

    public void makeCustomHeaderToNet() {
        if (this.customHeader != null) {
            Field[] fields = getClazzFields(customHeader.getClass());
            if (null == this.extFields) {
                this.extFields = new HashMap<String, String>();
            }

            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();
                    if (!name.startsWith("this")) {
                        Object value = null;
                        try {
                            field.setAccessible(true);
                            value = field.get(this.customHeader);
                        } catch (Exception e) {
                           // log.error("Failed to access field [{}]", name, e);
                        }

                        if (value != null) {
                            this.extFields.put(name, value.toString());
                        }
                    }
                }
            }
        }
    }
    private Field[] getClazzFields(Class<? extends CommandCustomHeader> classHeader) {
        Field[] field = null;// CLASS_HASH_MAP.get(classHeader);

        if (field == null) {
            field = classHeader.getDeclaredFields();
//            synchronized (CLASS_HASH_MAP) {
//                CLASS_HASH_MAP.put(classHeader, field);
//            }
        }
        return field;
    }
    private void writeHeaderLength(ByteBuf buf, int headerLenght) {
        byte[] result = new byte[4];

        result[0] = 0;
        result[1] = (byte) ((headerLenght >> 16) & 0xFF);
        result[2] = (byte) ((headerLenght >> 8) & 0xFF);
        result[3] = (byte) (headerLenght & 0xFF);

        buf.writeBytes(result);
    }

    public static RemotingCommand decode(ByteBuf buf) {
        int lenght = buf.capacity();
        System.out.println("all lenght = " + lenght);
        int orgHeaderLenght = buf.readInt();
        int headerLength = getHeaderLength(orgHeaderLenght);
        System.out.println(headerLength);
        byte []headerData = new byte[headerLength];
        buf.readBytes(headerData);

        int bodyLenght = lenght - headerLength - 4;
        byte []body = new byte[bodyLenght];
        buf.readBytes(body);
        System.out.println("bodylenght="+bodyLenght);
        RemotingCommand response = RemotingSerializable.unserialize(headerData);
        //TopicRouteData data = JSON
        System.out.println(response);
        return response;
    }
    public static int getHeaderLength(int length) {
        return length & 0xFFFFFF;
    }

    @Override
    public String toString() {
        return "RemotingCommand{" +
                "code=" + code +
                ", language=" + language +
                ", version=" + version +
                ", opaque=" + opaque +
                ", flag=" + flag +
                ", remark='" + remark + '\'' +
                ", extFields=" + extFields +
                ", customHeader=" + customHeader +
                ", serializeTypeCurrentRPC=" + serializeTypeCurrentRPC +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
