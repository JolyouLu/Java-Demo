// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: rpc.proto

package top.jolyoulu.demo.protobuf.protobufrpc.proto;

public interface RPCResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:top.jolyoulu.protobufrpc.proto.RPCResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 id = 1;</code>
   */
  long getId();

  /**
   * <code>string serviceName = 2;</code>
   */
  String getServiceName();
  /**
   * <code>string serviceName = 2;</code>
   */
  com.google.protobuf.ByteString
      getServiceNameBytes();

  /**
   * <code>string methodName = 3;</code>
   */
  String getMethodName();
  /**
   * <code>string methodName = 3;</code>
   */
  com.google.protobuf.ByteString
      getMethodNameBytes();

  /**
   * <code>string result = 4;</code>
   */
  String getResult();
  /**
   * <code>string result = 4;</code>
   */
  com.google.protobuf.ByteString
      getResultBytes();

  /**
   * <code>int64 version = 5;</code>
   */
  long getVersion();
}