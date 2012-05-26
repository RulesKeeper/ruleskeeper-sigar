/*
 * RulesKeeper, Monitoring System
 * Copyright (C) 2011-2012 RulesKeeper
 * mailto:contact AT ruleskeeper DOT org
 *
 * RulesKeeper is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * RulesKeeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with RulesKeeper; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: metric.proto

package org.ruleskeeper;

public final class MetricProtoBuf {
	private MetricProtoBuf() {
	}

	public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
	}

	public interface MetricOrBuilder extends com.google.protobuf.MessageOrBuilder {

		// optional string dataProvider = 1;
		boolean hasDataProvider();

		String getDataProvider();

		// required string key = 2;
		boolean hasKey();

		String getKey();

		// required string value = 3;
		boolean hasValue();

		String getValue();

		// required .org.ruleskeeper.Metric.UnitType unit = 4 [default = STRING];
		boolean hasUnit();

		org.ruleskeeper.MetricProtoBuf.Metric.UnitType getUnit();

		// repeated string context = 5;
		java.util.List<String> getContextList();

		int getContextCount();

		String getContext(int index);
	}

	public static final class Metric extends com.google.protobuf.GeneratedMessage implements MetricOrBuilder {
		// Use Metric.newBuilder() to construct.
		private Metric(Builder builder) {
			super(builder);
		}

		private Metric(boolean noInit) {
		}

		private static final Metric defaultInstance;

		public static Metric getDefaultInstance() {
			return defaultInstance;
		}

		public Metric getDefaultInstanceForType() {
			return defaultInstance;
		}

		public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
			return org.ruleskeeper.MetricProtoBuf.internal_static_org_ruleskeeper_Metric_descriptor;
		}

		protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
			return org.ruleskeeper.MetricProtoBuf.internal_static_org_ruleskeeper_Metric_fieldAccessorTable;
		}

		public enum UnitType
		                    implements
		                    com.google.protobuf.ProtocolMessageEnum {
			COUNTER(
			        0,
			        0),
			PERCENT(
			        1,
			        1),
			MB(
			   2,
			   2),
			STRING(
			       3,
			       3),
			MILLISECONDS(
			             4,
			             4),
			SECONDS(
			        5,
			        5), ;

			public static final int COUNTER_VALUE = 0;
			public static final int PERCENT_VALUE = 1;
			public static final int MB_VALUE = 2;
			public static final int STRING_VALUE = 3;
			public static final int MILLISECONDS_VALUE = 4;
			public static final int SECONDS_VALUE = 5;

			public final int getNumber() {
				return value;
			}

			public static UnitType valueOf(int value) {
				switch (value) {
				case 0:
					return COUNTER;
				case 1:
					return PERCENT;
				case 2:
					return MB;
				case 3:
					return STRING;
				case 4:
					return MILLISECONDS;
				case 5:
					return SECONDS;
				default:
					return null;
				}
			}

			public static com.google.protobuf.Internal.EnumLiteMap<UnitType> internalGetValueMap() {
				return internalValueMap;
			}

			private static com.google.protobuf.Internal.EnumLiteMap<UnitType> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<UnitType>() {
				public UnitType findValueByNumber(int number) {
					return UnitType.valueOf(number);
				}
			};

			public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
				return getDescriptor().getValues().get(index);
			}

			public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
				return getDescriptor();
			}

			public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
				return org.ruleskeeper.MetricProtoBuf.Metric.getDescriptor().getEnumTypes().get(0);
			}

			private static final UnitType[] VALUES = { COUNTER, PERCENT, MB, STRING, MILLISECONDS, SECONDS, };

			public static UnitType valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
				if (desc.getType() != getDescriptor()) {
					throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
				}
				return VALUES[desc.getIndex()];
			}

			private final int index;
			private final int value;

			private UnitType(int index, int value) {
				this.index = index;
				this.value = value;
			}

			// @@protoc_insertion_point(enum_scope:org.ruleskeeper.Metric.UnitType)
		}

		private int bitField0_;
		// optional string dataProvider = 1;
		public static final int DATAPROVIDER_FIELD_NUMBER = 1;
		private java.lang.Object dataProvider_;

		public boolean hasDataProvider() {
			return ((bitField0_ & 0x00000001) == 0x00000001);
		}

		public String getDataProvider() {
			java.lang.Object ref = dataProvider_;
			if (ref instanceof String) {
				return (String) ref;
			} else {
				com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
				String s = bs.toStringUtf8();
				if (com.google.protobuf.Internal.isValidUtf8(bs)) {
					dataProvider_ = s;
				}
				return s;
			}
		}

		private com.google.protobuf.ByteString getDataProviderBytes() {
			java.lang.Object ref = dataProvider_;
			if (ref instanceof String) {
				com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8((String) ref);
				dataProvider_ = b;
				return b;
			} else {
				return (com.google.protobuf.ByteString) ref;
			}
		}

		// required string key = 2;
		public static final int KEY_FIELD_NUMBER = 2;
		private java.lang.Object key_;

		public boolean hasKey() {
			return ((bitField0_ & 0x00000002) == 0x00000002);
		}

		public String getKey() {
			java.lang.Object ref = key_;
			if (ref instanceof String) {
				return (String) ref;
			} else {
				com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
				String s = bs.toStringUtf8();
				if (com.google.protobuf.Internal.isValidUtf8(bs)) {
					key_ = s;
				}
				return s;
			}
		}

		private com.google.protobuf.ByteString getKeyBytes() {
			java.lang.Object ref = key_;
			if (ref instanceof String) {
				com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8((String) ref);
				key_ = b;
				return b;
			} else {
				return (com.google.protobuf.ByteString) ref;
			}
		}

		// required string value = 3;
		public static final int VALUE_FIELD_NUMBER = 3;
		private java.lang.Object value_;

		public boolean hasValue() {
			return ((bitField0_ & 0x00000004) == 0x00000004);
		}

		public String getValue() {
			java.lang.Object ref = value_;
			if (ref instanceof String) {
				return (String) ref;
			} else {
				com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
				String s = bs.toStringUtf8();
				if (com.google.protobuf.Internal.isValidUtf8(bs)) {
					value_ = s;
				}
				return s;
			}
		}

		private com.google.protobuf.ByteString getValueBytes() {
			java.lang.Object ref = value_;
			if (ref instanceof String) {
				com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8((String) ref);
				value_ = b;
				return b;
			} else {
				return (com.google.protobuf.ByteString) ref;
			}
		}

		// required .org.ruleskeeper.Metric.UnitType unit = 4 [default = STRING];
		public static final int UNIT_FIELD_NUMBER = 4;
		private org.ruleskeeper.MetricProtoBuf.Metric.UnitType unit_;

		public boolean hasUnit() {
			return ((bitField0_ & 0x00000008) == 0x00000008);
		}

		public org.ruleskeeper.MetricProtoBuf.Metric.UnitType getUnit() {
			return unit_;
		}

		// repeated string context = 5;
		public static final int CONTEXT_FIELD_NUMBER = 5;
		private com.google.protobuf.LazyStringList context_;

		public java.util.List<String> getContextList() {
			return context_;
		}

		public int getContextCount() {
			return context_.size();
		}

		public String getContext(int index) {
			return context_.get(index);
		}

		private void initFields() {
			dataProvider_ = "";
			key_ = "";
			value_ = "";
			unit_ = org.ruleskeeper.MetricProtoBuf.Metric.UnitType.STRING;
			context_ = com.google.protobuf.LazyStringArrayList.EMPTY;
		}

		private byte memoizedIsInitialized = -1;

		public final boolean isInitialized() {
			byte isInitialized = memoizedIsInitialized;
			if (isInitialized != -1)
				return isInitialized == 1;

			if (!hasKey()) {
				memoizedIsInitialized = 0;
				return false;
			}
			if (!hasValue()) {
				memoizedIsInitialized = 0;
				return false;
			}
			if (!hasUnit()) {
				memoizedIsInitialized = 0;
				return false;
			}
			memoizedIsInitialized = 1;
			return true;
		}

		public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
			getSerializedSize();
			if (((bitField0_ & 0x00000001) == 0x00000001)) {
				output.writeBytes(1, getDataProviderBytes());
			}
			if (((bitField0_ & 0x00000002) == 0x00000002)) {
				output.writeBytes(2, getKeyBytes());
			}
			if (((bitField0_ & 0x00000004) == 0x00000004)) {
				output.writeBytes(3, getValueBytes());
			}
			if (((bitField0_ & 0x00000008) == 0x00000008)) {
				output.writeEnum(4, unit_.getNumber());
			}
			for (int i = 0; i < context_.size(); i++) {
				output.writeBytes(5, context_.getByteString(i));
			}
			getUnknownFields().writeTo(output);
		}

		private int memoizedSerializedSize = -1;

		public int getSerializedSize() {
			int size = memoizedSerializedSize;
			if (size != -1)
				return size;

			size = 0;
			if (((bitField0_ & 0x00000001) == 0x00000001)) {
				size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getDataProviderBytes());
			}
			if (((bitField0_ & 0x00000002) == 0x00000002)) {
				size += com.google.protobuf.CodedOutputStream.computeBytesSize(2, getKeyBytes());
			}
			if (((bitField0_ & 0x00000004) == 0x00000004)) {
				size += com.google.protobuf.CodedOutputStream.computeBytesSize(3, getValueBytes());
			}
			if (((bitField0_ & 0x00000008) == 0x00000008)) {
				size += com.google.protobuf.CodedOutputStream.computeEnumSize(4, unit_.getNumber());
			}
			{
				int dataSize = 0;
				for (int i = 0; i < context_.size(); i++) {
					dataSize += com.google.protobuf.CodedOutputStream.computeBytesSizeNoTag(context_.getByteString(i));
				}
				size += dataSize;
				size += 1 * getContextList().size();
			}
			size += getUnknownFields().getSerializedSize();
			memoizedSerializedSize = size;
			return size;
		}

		private static final long serialVersionUID = 0L;

		@java.lang.Override
		protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
			return super.writeReplace();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
		    throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
		    throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(java.io.InputStream input) throws java.io.IOException {
			return newBuilder().mergeFrom(input).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
		    throws java.io.IOException {
			return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
			Builder builder = newBuilder();
			if (builder.mergeDelimitedFrom(input)) {
				return builder.buildParsed();
			} else {
				return null;
			}
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
		    throws java.io.IOException {
			Builder builder = newBuilder();
			if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
				return builder.buildParsed();
			} else {
				return null;
			}
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
			return newBuilder().mergeFrom(input).buildParsed();
		}

		public static org.ruleskeeper.MetricProtoBuf.Metric parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
		    throws java.io.IOException {
			return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(org.ruleskeeper.MetricProtoBuf.Metric prototype) {
			return newBuilder().mergeFrom(prototype);
		}

		public Builder toBuilder() {
			return newBuilder(this);
		}

		@java.lang.Override
		protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
			Builder builder = new Builder(parent);
			return builder;
		}

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements org.ruleskeeper.MetricProtoBuf.MetricOrBuilder {
			public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
				return org.ruleskeeper.MetricProtoBuf.internal_static_org_ruleskeeper_Metric_descriptor;
			}

			protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
				return org.ruleskeeper.MetricProtoBuf.internal_static_org_ruleskeeper_Metric_fieldAccessorTable;
			}

			// Construct using org.ruleskeeper.MetricProtoBuf.Metric.newBuilder()
			private Builder() {
				maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
				}
			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				dataProvider_ = "";
				bitField0_ = (bitField0_ & ~0x00000001);
				key_ = "";
				bitField0_ = (bitField0_ & ~0x00000002);
				value_ = "";
				bitField0_ = (bitField0_ & ~0x00000004);
				unit_ = org.ruleskeeper.MetricProtoBuf.Metric.UnitType.STRING;
				bitField0_ = (bitField0_ & ~0x00000008);
				context_ = com.google.protobuf.LazyStringArrayList.EMPTY;
				bitField0_ = (bitField0_ & ~0x00000010);
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(buildPartial());
			}

			public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
				return org.ruleskeeper.MetricProtoBuf.Metric.getDescriptor();
			}

			public org.ruleskeeper.MetricProtoBuf.Metric getDefaultInstanceForType() {
				return org.ruleskeeper.MetricProtoBuf.Metric.getDefaultInstance();
			}

			public org.ruleskeeper.MetricProtoBuf.Metric build() {
				org.ruleskeeper.MetricProtoBuf.Metric result = buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				}
				return result;
			}

			private org.ruleskeeper.MetricProtoBuf.Metric buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
				org.ruleskeeper.MetricProtoBuf.Metric result = buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
				}
				return result;
			}

			public org.ruleskeeper.MetricProtoBuf.Metric buildPartial() {
				org.ruleskeeper.MetricProtoBuf.Metric result = new org.ruleskeeper.MetricProtoBuf.Metric(this);
				int from_bitField0_ = bitField0_;
				int to_bitField0_ = 0;
				if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
					to_bitField0_ |= 0x00000001;
				}
				result.dataProvider_ = dataProvider_;
				if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
					to_bitField0_ |= 0x00000002;
				}
				result.key_ = key_;
				if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
					to_bitField0_ |= 0x00000004;
				}
				result.value_ = value_;
				if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
					to_bitField0_ |= 0x00000008;
				}
				result.unit_ = unit_;
				if (((bitField0_ & 0x00000010) == 0x00000010)) {
					context_ = new com.google.protobuf.UnmodifiableLazyStringList(context_);
					bitField0_ = (bitField0_ & ~0x00000010);
				}
				result.context_ = context_;
				result.bitField0_ = to_bitField0_;
				onBuilt();
				return result;
			}

			public Builder mergeFrom(com.google.protobuf.Message other) {
				if (other instanceof org.ruleskeeper.MetricProtoBuf.Metric) {
					return mergeFrom((org.ruleskeeper.MetricProtoBuf.Metric) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(org.ruleskeeper.MetricProtoBuf.Metric other) {
				if (other == org.ruleskeeper.MetricProtoBuf.Metric.getDefaultInstance())
					return this;
				if (other.hasDataProvider()) {
					setDataProvider(other.getDataProvider());
				}
				if (other.hasKey()) {
					setKey(other.getKey());
				}
				if (other.hasValue()) {
					setValue(other.getValue());
				}
				if (other.hasUnit()) {
					setUnit(other.getUnit());
				}
				if (!other.context_.isEmpty()) {
					if (context_.isEmpty()) {
						context_ = other.context_;
						bitField0_ = (bitField0_ & ~0x00000010);
					} else {
						ensureContextIsMutable();
						context_.addAll(other.context_);
					}
					onChanged();
				}
				this.mergeUnknownFields(other.getUnknownFields());
				return this;
			}

			public final boolean isInitialized() {
				if (!hasKey()) {

					return false;
				}
				if (!hasValue()) {

					return false;
				}
				if (!hasUnit()) {

					return false;
				}
				return true;
			}

			public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
				com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
				while (true) {
					int tag = input.readTag();
					switch (tag) {
					case 0:
						this.setUnknownFields(unknownFields.build());
						onChanged();
						return this;
					default: {
						if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
							this.setUnknownFields(unknownFields.build());
							onChanged();
							return this;
						}
						break;
					}
					case 10: {
						bitField0_ |= 0x00000001;
						dataProvider_ = input.readBytes();
						break;
					}
					case 18: {
						bitField0_ |= 0x00000002;
						key_ = input.readBytes();
						break;
					}
					case 26: {
						bitField0_ |= 0x00000004;
						value_ = input.readBytes();
						break;
					}
					case 32: {
						int rawValue = input.readEnum();
						org.ruleskeeper.MetricProtoBuf.Metric.UnitType value = org.ruleskeeper.MetricProtoBuf.Metric.UnitType.valueOf(rawValue);
						if (value == null) {
							unknownFields.mergeVarintField(4, rawValue);
						} else {
							bitField0_ |= 0x00000008;
							unit_ = value;
						}
						break;
					}
					case 42: {
						ensureContextIsMutable();
						context_.add(input.readBytes());
						break;
					}
					}
				}
			}

			private int bitField0_;

			// optional string dataProvider = 1;
			private java.lang.Object dataProvider_ = "";

			public boolean hasDataProvider() {
				return ((bitField0_ & 0x00000001) == 0x00000001);
			}

			public String getDataProvider() {
				java.lang.Object ref = dataProvider_;
				if (!(ref instanceof String)) {
					String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
					dataProvider_ = s;
					return s;
				} else {
					return (String) ref;
				}
			}

			public Builder setDataProvider(String value) {
				if (value == null) {
					throw new NullPointerException();
				}
				bitField0_ |= 0x00000001;
				dataProvider_ = value;
				onChanged();
				return this;
			}

			public Builder clearDataProvider() {
				bitField0_ = (bitField0_ & ~0x00000001);
				dataProvider_ = getDefaultInstance().getDataProvider();
				onChanged();
				return this;
			}

			void setDataProvider(com.google.protobuf.ByteString value) {
				bitField0_ |= 0x00000001;
				dataProvider_ = value;
				onChanged();
			}

			// required string key = 2;
			private java.lang.Object key_ = "";

			public boolean hasKey() {
				return ((bitField0_ & 0x00000002) == 0x00000002);
			}

			public String getKey() {
				java.lang.Object ref = key_;
				if (!(ref instanceof String)) {
					String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
					key_ = s;
					return s;
				} else {
					return (String) ref;
				}
			}

			public Builder setKey(String value) {
				if (value == null) {
					throw new NullPointerException();
				}
				bitField0_ |= 0x00000002;
				key_ = value;
				onChanged();
				return this;
			}

			public Builder clearKey() {
				bitField0_ = (bitField0_ & ~0x00000002);
				key_ = getDefaultInstance().getKey();
				onChanged();
				return this;
			}

			void setKey(com.google.protobuf.ByteString value) {
				bitField0_ |= 0x00000002;
				key_ = value;
				onChanged();
			}

			// required string value = 3;
			private java.lang.Object value_ = "";

			public boolean hasValue() {
				return ((bitField0_ & 0x00000004) == 0x00000004);
			}

			public String getValue() {
				java.lang.Object ref = value_;
				if (!(ref instanceof String)) {
					String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
					value_ = s;
					return s;
				} else {
					return (String) ref;
				}
			}

			public Builder setValue(String value) {
				if (value == null) {
					throw new NullPointerException();
				}
				bitField0_ |= 0x00000004;
				value_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue() {
				bitField0_ = (bitField0_ & ~0x00000004);
				value_ = getDefaultInstance().getValue();
				onChanged();
				return this;
			}

			void setValue(com.google.protobuf.ByteString value) {
				bitField0_ |= 0x00000004;
				value_ = value;
				onChanged();
			}

			// required .org.ruleskeeper.Metric.UnitType unit = 4 [default = STRING];
			private org.ruleskeeper.MetricProtoBuf.Metric.UnitType unit_ = org.ruleskeeper.MetricProtoBuf.Metric.UnitType.STRING;

			public boolean hasUnit() {
				return ((bitField0_ & 0x00000008) == 0x00000008);
			}

			public org.ruleskeeper.MetricProtoBuf.Metric.UnitType getUnit() {
				return unit_;
			}

			public Builder setUnit(org.ruleskeeper.MetricProtoBuf.Metric.UnitType value) {
				if (value == null) {
					throw new NullPointerException();
				}
				bitField0_ |= 0x00000008;
				unit_ = value;
				onChanged();
				return this;
			}

			public Builder clearUnit() {
				bitField0_ = (bitField0_ & ~0x00000008);
				unit_ = org.ruleskeeper.MetricProtoBuf.Metric.UnitType.STRING;
				onChanged();
				return this;
			}

			// repeated string context = 5;
			private com.google.protobuf.LazyStringList context_ = com.google.protobuf.LazyStringArrayList.EMPTY;

			private void ensureContextIsMutable() {
				if (!((bitField0_ & 0x00000010) == 0x00000010)) {
					context_ = new com.google.protobuf.LazyStringArrayList(context_);
					bitField0_ |= 0x00000010;
				}
			}

			public java.util.List<String> getContextList() {
				return java.util.Collections.unmodifiableList(context_);
			}

			public int getContextCount() {
				return context_.size();
			}

			public String getContext(int index) {
				return context_.get(index);
			}

			public Builder setContext(int index, String value) {
				if (value == null) {
					throw new NullPointerException();
				}
				ensureContextIsMutable();
				context_.set(index, value);
				onChanged();
				return this;
			}

			public Builder addContext(String value) {
				if (value == null) {
					throw new NullPointerException();
				}
				ensureContextIsMutable();
				context_.add(value);
				onChanged();
				return this;
			}

			public Builder addAllContext(java.lang.Iterable<String> values) {
				ensureContextIsMutable();
				super.addAll(values, context_);
				onChanged();
				return this;
			}

			public Builder clearContext() {
				context_ = com.google.protobuf.LazyStringArrayList.EMPTY;
				bitField0_ = (bitField0_ & ~0x00000010);
				onChanged();
				return this;
			}

			void addContext(com.google.protobuf.ByteString value) {
				ensureContextIsMutable();
				context_.add(value);
				onChanged();
			}

			// @@protoc_insertion_point(builder_scope:org.ruleskeeper.Metric)
		}

		static {
			defaultInstance = new Metric(true);
			defaultInstance.initFields();
		}

		// @@protoc_insertion_point(class_scope:org.ruleskeeper.Metric)
	}

	private static com.google.protobuf.Descriptors.Descriptor internal_static_org_ruleskeeper_Metric_descriptor;
	private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_org_ruleskeeper_Metric_fieldAccessorTable;

	public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
		return descriptor;
	}

	private static com.google.protobuf.Descriptors.FileDescriptor descriptor;
	static {
		java.lang.String[] descriptorData = { "\n\014metric.proto\022\017org.ruleskeeper\"\334\001\n\006Metr"
		    + "ic\022\024\n\014dataProvider\030\001 \001(\t\022\013\n\003key\030\002 \002(\t\022\r\n" + "\005value\030\003 \002(\t\0226\n\004unit\030\004 \002(\0162 .org.ruleske"
		    + "eper.Metric.UnitType:\006STRING\022\017\n\007context\030" + "\005 \003(\t\"W\n\010UnitType\022\013\n\007COUNTER\020\000\022\013\n\007PERCEN"
		    + "T\020\001\022\006\n\002MB\020\002\022\n\n\006STRING\020\003\022\020\n\014MILLISECONDS\020" + "\004\022\013\n\007SECONDS\020\005B!\n\017org.ruleskeeperB\016Metri"
		    + "cProtoBuf" };
		com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
			public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
				descriptor = root;
				internal_static_org_ruleskeeper_Metric_descriptor = getDescriptor().getMessageTypes().get(0);
				internal_static_org_ruleskeeper_Metric_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_org_ruleskeeper_Metric_descriptor,
				    new java.lang.String[] { "DataProvider", "Key", "Value", "Unit", "Context", }, org.ruleskeeper.MetricProtoBuf.Metric.class,
				    org.ruleskeeper.MetricProtoBuf.Metric.Builder.class);
				return null;
			}
		};
		com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
	}

	// @@protoc_insertion_point(outer_class_scope)
}