/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <sys/time.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/HelloJni/HelloJni.java
 */
jlong
Java_com_bench_memtest_Memtest_memcpytestJNI(JNIEnv* env, jobject thiz,
                                        jint testsize,
                                        jint testcount) { 
    struct timeval start, end; 
    int *i1, *i2, i;
    i1 = (int*)malloc(testsize * sizeof(int));
    i2 = (int*)malloc(testsize * sizeof(int));

    gettimeofday(&start, NULL);
    for (i = 0; i < testcount; i++) {
        memcpy(i1, i2, testsize * sizeof(int));
    }
    gettimeofday(&end, NULL);

    free(i1);
    free(i2); 
    return (end.tv_sec - start.tv_sec) * 1000000 + end.tv_usec - start.tv_usec;
}
/*
jlong
Java_com_bench_memtest_Memtest_swaptestJNI(JNIEnv* env, jobject thiz,
                           jint testcount) {
    struct timeval start, end; 
    int i;
    int a, b, c;

    gettimeofday(&start, NULL);
    for (i = 0; i < testcount; i++) {
        c = a;
        a = b;
        b = c;
    }
    gettimeofday(&end, NULL);

    return (end.tv_sec - start.tv_sec) * 1000000 + end.tv_usec - start.tv_usec;
}
*/
