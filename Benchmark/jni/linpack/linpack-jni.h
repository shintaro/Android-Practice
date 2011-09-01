
#include <jni.h>

#ifdef DEBUG
#include <android/log.h>
#endif

char resultString[255];

jstring
Java_com_agatsuma_android_benchmark_Linpack_getPrecision(
	                                JNIEnv* env,
	                                jobject thiz) {
            return (*env)->NewStringUTF(env, PREC);
}

jint
Java_com_agatsuma_android_benchmark_Linpack_getMachinePrecision(
	                                JNIEnv* env,
	                                jobject thiz) {
            return BASE10DIG;
}


