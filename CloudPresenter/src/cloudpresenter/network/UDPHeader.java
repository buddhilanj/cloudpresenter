/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.utils.ByteConvert;
import java.text.ParseException;
import java.util.Date;
import javax.print.DocFlavor.BYTE_ARRAY;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class UDPHeader {

    public final static int metaDataSize = 18;
    public final static int bufferSize = 65000;
    public final static int ignore =0;
    private Date idtag;
    private long timestamp;
    private byte count;
    private byte type;
    private int size;
    private int fullsize;

    public UDPHeader() {
        
    }


    public UDPHeader(long timestamp, byte type, int fullsize) {
        this.idtag = new Date(timestamp);
        this.timestamp = timestamp;
        this.count = 0;
        this.type = type;
        this.size = 0;
        this.fullsize = fullsize;
    }

    public UDPHeader(Date idtag, byte type, int fullsize) {
        this.idtag = idtag;
        this.timestamp = idtag.getTime();
        this.count = 0;
        this.type = type;
        this.size = 0;
        this.fullsize = fullsize;
    }

    public byte[] generateNextMetaFile() throws ParseException {
        if (idtag == null) {
            throw new ParseException("Time Stamp Not set", bufferSize);
        } else if (count < 0) {
            throw new ParseException("Invalid count:" + count, bufferSize);
        } else if (type < 0) {
            throw new ParseException("Invalid type:" + type, bufferSize);
        } else if (size <= 0) {
            throw new ParseException("Invalid size:" + size, bufferSize);
        } else if (this.fullsize <= 0) {
            throw new ParseException("Invalid size:" + this.fullsize, bufferSize);
        } else {
            byte[] metadata = new byte[metaDataSize];
            byte[] timestamparr = ByteConvert.longToByteArr(this.timestamp);
            System.arraycopy(timestamparr, 0, metadata, 0, timestamparr.length);
            metadata[8] = type;
            metadata[9] = count;
            byte[] sizearr = ByteConvert.intToByteArr(this.size);
            System.arraycopy(sizearr, 0, metadata, 10, sizearr.length);
            byte[] fullsizearr = ByteConvert.intToByteArr(this.fullsize);
            System.arraycopy(fullsizearr, 0, metadata, 14, fullsizearr.length);
            return metadata;
        }
    }

    public void decodeMetaData(byte[] metadata) throws ParseException {
        if (metadata.length != metaDataSize) {
            throw new ParseException("Meta file is does not fit the specified size:" + metaDataSize, bufferSize);
        }
        byte[] timestamparr = new byte[8];
        System.arraycopy(metadata, 0, timestamparr, 0, 8);
        this.setTimestamp(ByteConvert.byteArrToLong(timestamparr));
        this.type = metadata[8];
        this.count = metadata[9];

        byte[] sizearr = new byte[4];
        System.arraycopy(metadata, 10, sizearr, 0, sizearr.length);
        this.size = ByteConvert.byteArrToInt(sizearr);
        byte[] fullsizearr = new byte[4];
        System.arraycopy(metadata, 14, fullsizearr, 0, fullsizearr.length);
        this.fullsize = ByteConvert.byteArrToInt(fullsizearr);
    }

    public void extractMetaDataFromPacket(byte[] packet) throws ParseException{
        byte[] metadata = new byte[metaDataSize];
        System.arraycopy(packet, ignore, metadata, 0, metaDataSize);
        decodeMetaData(metadata);
    }

    /**
     * @return the idtag
     */
    public Date getIdtag() {
        return idtag;
    }

    /**
     * @param idtag the idtag to set
     */
    public void setIdtag(Date idtag) {
        this.idtag = idtag;
        this.timestamp = idtag.getTime();
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        this.idtag = new Date(timestamp);
    }

    /**
     * @return the count
     */
    public byte getCount() {
        return count;
    }

    /**
     * increment the count
     */
    public void incrementCount() {
        this.count++;
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the fullzie
     */
    public int getFullsize() {
        return fullsize;
    }
}
