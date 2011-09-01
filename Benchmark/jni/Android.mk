# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := linpack-jni
LOCAL_SRC_FILES := linpack/linpack-jni.c
LOCAL_CFLAGS    := -Werror -O3 -DSP -DROLL -lm
LOCAL_LDLIBS    := -llog -lm

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE    := stream-jni
LOCAL_SRC_FILES := stream/stream-jni.c
LOCAL_CFLAGS    := -Werror -O3 -lm
#LOCAL_CFLAGS    := -Werror -O3 msse3 -march=core2 -mfpmath=sse -lm
#LOCAL_LDLIBS    := -llog -lm

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE    := latency-jni
LOCAL_SRC_FILES := latency/lat_mem_rd.c latency/lib_mem.c latency/lib_sched.c latency/lib_timing.c latency/lib_stats.c latency/lib_unix.c latency/getopt.c
#LOCAL_CFLAGS    := -Werror -O3 -DRUSAGE -DHAVE_uint=1 -DHAVE_int64_t=1 -DHAVE_pmap_clnt_h -DHAVE_socklen_t -DHAVE_DRAND48 -DHAVE_SCHED_SETAFFINITY=1 -lm
LOCAL_CFLAGS    := -Werror -O3 -DHAVE_socklen_t -DHAVE_DRAND48 -lm
LOCAL_LDLIBS    := -llog -lm

include $(BUILD_SHARED_LIBRARY)

