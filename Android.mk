LOCAL_PATH:= $(call my-dir)
# Build the Phone app which includes the emergency dialer. See Contacts
# for the 'other' dialer.
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := test

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SARTool
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)

# Build the test package
include $(call all-makefiles-under,$(LOCAL_PATH))
