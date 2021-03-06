// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: wallet.proto

package com.wallet.proto;

/**
 * Protobuf enum {@code STATUS}
 */
public enum STATUS
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>TRANSACTION_SUCCESS = 0;</code>
   */
  TRANSACTION_SUCCESS(0),
  /**
   * <code>TRANSACTION_FAILED = 1;</code>
   */
  TRANSACTION_FAILED(1),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>TRANSACTION_SUCCESS = 0;</code>
   */
  public static final int TRANSACTION_SUCCESS_VALUE = 0;
  /**
   * <code>TRANSACTION_FAILED = 1;</code>
   */
  public static final int TRANSACTION_FAILED_VALUE = 1;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static STATUS valueOf(int value) {
    return forNumber(value);
  }

  public static STATUS forNumber(int value) {
    switch (value) {
      case 0: return TRANSACTION_SUCCESS;
      case 1: return TRANSACTION_FAILED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<STATUS>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      STATUS> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<STATUS>() {
          public STATUS findValueByNumber(int number) {
            return STATUS.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.wallet.proto.WalletClass.getDescriptor().getEnumTypes().get(0);
  }

  private static final STATUS[] VALUES = values();

  public static STATUS valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private STATUS(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:STATUS)
}

