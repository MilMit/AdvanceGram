/*
 *  Copyright (c) 2022 The WebM project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

#include <assert.h>
#include "./vpx_dsp_rtcd.h"
#include "vpx_dsp/loongarch/vpx_convolve_lsx.h"

#define LSX_LD_4(_src, _stride, _src0, _src1, _src2, _src3) \
  {                                                         \
    _src0 = __lsx_vld(_src, 0);                             \
    _src += _stride;                                        \
    _src1 = __lsx_vld(_src, 0);                             \
    _src += _stride;                                        \
    _src2 = __lsx_vld(_src, 0);                             \
    _src += _stride;                                        \
    _src3 = __lsx_vld(_src, 0);                             \
  }

static const uint8_t mc_filt_mask_arr[16 * 3] = {
  /* 8 width cases */
  0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8,
  /* 4 width cases */
  0, 1, 1, 2, 2, 3, 3, 4, 16, 17, 17, 18, 18, 19, 19, 20,
  /* 4 width cases */
  8, 9, 9, 10, 10, 11, 11, 12, 24, 25, 25, 26, 26, 27, 27, 28
};

static void common_hv_8ht_8vt_4w_lsx(const uint8_t *src, int32_t src_stride,
                                     uint8_t *dst, int32_t dst_stride,
                                     int8_t *filter_horiz, int8_t *filter_vert,
                                     int32_t height) {
  uint32_t loop_cnt = (height >> 2);
  __m128i src0, src1, src2, src3, src4, src5, src6, src7, src8, src9, src10;
  __m128i filt_hz0, filt_hz1, filt_hz2, filt_hz3;
  __m128i filt_vt0, filt_vt1, filt_vt2, filt_vt3;
  __m128i mask0, mask1, mask2, mask3;
  __m128i tmp0, tmp1, tmp2, tmp3, tmp4, tmp5;
  __m128i out0, out1;
  __m128i shuff = { 0x0F0E0D0C0B0A0908, 0x1716151413121110 };

  mask0 = __lsx_vld(mc_filt_mask_arr, 16);
  src -= (3 + 3 * src_stride);
  DUP4_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_horiz, 2, filter_horiz, 4,
            filter_horiz, 6, filt_hz0, filt_hz1, filt_hz2, filt_hz3);
  DUP2_ARG2(__lsx_vaddi_bu, mask0, 2, mask0, 4, mask1, mask2);
  mask3 = __lsx_vaddi_bu(mask0, 6);

  LSX_LD_4(src, src_stride, src0, src1, src2, src3);
  src += src_stride;
  src4 = __lsx_vld(src, 0);
  src += src_stride;
  src5 = __lsx_vld(src, 0);
  src += src_stride;
  src6 = __lsx_vld(src, 0);
  src += src_stride;
  DUP4_ARG2(__lsx_vxori_b, src0, 128, src1, 128, src2, 128, src3, 128, src0,
            src1, src2, src3);
  DUP2_ARG2(__lsx_vxori_b, src4, 128, src5, 128, src4, src5);
  src6 = __lsx_vxori_b(src6, 128);

  tmp0 = HORIZ_8TAP_FILT(src0, src1, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  tmp2 = HORIZ_8TAP_FILT(src2, src3, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  tmp4 = HORIZ_8TAP_FILT(src4, src5, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  tmp5 = HORIZ_8TAP_FILT(src5, src6, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  DUP2_ARG3(__lsx_vshuf_b, tmp2, tmp0, shuff, tmp4, tmp2, shuff, tmp1, tmp3);
  DUP4_ARG2(__lsx_vldrepl_h, filter_vert, 0, filter_vert, 2, filter_vert, 4,
            filter_vert, 6, filt_vt0, filt_vt1, filt_vt2, filt_vt3);
  DUP2_ARG2(__lsx_vpackev_b, tmp1, tmp0, tmp3, tmp2, tmp0, tmp1);
  tmp2 = __lsx_vpackev_b(tmp5, tmp4);

  for (; loop_cnt--;) {
    LSX_LD_4(src, src_stride, src7, src8, src9, src10);
    src += src_stride;
    DUP4_ARG2(__lsx_vxori_b, src7, 128, src8, 128, src9, 128, src10, 128, src7,
              src8, src9, src10);
    tmp3 = HORIZ_8TAP_FILT(src7, src8, mask0, mask1, mask2, mask3, filt_hz0,
                           filt_hz1, filt_hz2, filt_hz3);
    tmp4 = __lsx_vshuf_b(tmp3, tmp5, shuff);
    tmp4 = __lsx_vpackev_b(tmp3, tmp4);
    out0 = FILT_8TAP_DPADD_S_H(tmp0, tmp1, tmp2, tmp4, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    src1 = HORIZ_8TAP_FILT(src9, src10, mask0, mask1, mask2, mask3, filt_hz0,
                           filt_hz1, filt_hz2, filt_hz3);
    src0 = __lsx_vshuf_b(src1, tmp3, shuff);
    src0 = __lsx_vpackev_b(src1, src0);
    out1 = FILT_8TAP_DPADD_S_H(tmp1, tmp2, tmp4, src0, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    out0 = __lsx_vssrarni_b_h(out1, out0, 7);
    out0 = __lsx_vxori_b(out0, 128);
    __lsx_vstelm_w(out0, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_w(out0, dst, 0, 1);
    dst += dst_stride;
    __lsx_vstelm_w(out0, dst, 0, 2);
    dst += dst_stride;
    __lsx_vstelm_w(out0, dst, 0, 3);
    dst += dst_stride;

    tmp5 = src1;
    tmp0 = tmp2;
    tmp1 = tmp4;
    tmp2 = src0;
  }
}

static void common_hv_8ht_8vt_8w_lsx(const uint8_t *src, int32_t src_stride,
                                     uint8_t *dst, int32_t dst_stride,
                                     int8_t *filter_horiz, int8_t *filter_vert,
                                     int32_t height) {
  uint32_t loop_cnt = (height >> 2);
  __m128i src0, src1, src2, src3, src4, src5, src6, src7, src8, src9, src10;
  __m128i filt_hz0, filt_hz1, filt_hz2, filt_hz3;
  __m128i filt_vt0, filt_vt1, filt_vt2, filt_vt3;
  __m128i mask0, mask1, mask2, mask3;
  __m128i tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6;
  __m128i out0, out1;

  mask0 = __lsx_vld(mc_filt_mask_arr, 0);
  src -= (3 + 3 * src_stride);
  DUP4_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_horiz, 2, filter_horiz, 4,
            filter_horiz, 6, filt_hz0, filt_hz1, filt_hz2, filt_hz3);
  DUP2_ARG2(__lsx_vaddi_bu, mask0, 2, mask0, 4, mask1, mask2);
  mask3 = __lsx_vaddi_bu(mask0, 6);

  LSX_LD_4(src, src_stride, src0, src1, src2, src3);
  src += src_stride;
  src4 = __lsx_vld(src, 0);
  src += src_stride;
  src5 = __lsx_vld(src, 0);
  src += src_stride;
  src6 = __lsx_vld(src, 0);
  src += src_stride;
  DUP4_ARG2(__lsx_vxori_b, src0, 128, src1, 128, src2, 128, src3, 128, src0,
            src1, src2, src3);
  DUP2_ARG2(__lsx_vxori_b, src4, 128, src5, 128, src4, src5);
  src6 = __lsx_vxori_b(src6, 128);

  src0 = HORIZ_8TAP_FILT(src0, src0, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src1 = HORIZ_8TAP_FILT(src1, src1, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src2 = HORIZ_8TAP_FILT(src2, src2, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src3 = HORIZ_8TAP_FILT(src3, src3, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src4 = HORIZ_8TAP_FILT(src4, src4, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src5 = HORIZ_8TAP_FILT(src5, src5, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);
  src6 = HORIZ_8TAP_FILT(src6, src6, mask0, mask1, mask2, mask3, filt_hz0,
                         filt_hz1, filt_hz2, filt_hz3);

  DUP4_ARG2(__lsx_vldrepl_h, filter_vert, 0, filter_vert, 2, filter_vert, 4,
            filter_vert, 6, filt_vt0, filt_vt1, filt_vt2, filt_vt3);
  DUP4_ARG2(__lsx_vpackev_b, src1, src0, src3, src2, src5, src4, src2, src1,
            tmp0, tmp1, tmp2, tmp4);
  DUP2_ARG2(__lsx_vpackev_b, src4, src3, src6, src5, tmp5, tmp6);

  for (; loop_cnt--;) {
    LSX_LD_4(src, src_stride, src7, src8, src9, src10);
    src += src_stride;
    DUP4_ARG2(__lsx_vxori_b, src7, 128, src8, 128, src9, 128, src10, 128, src7,
              src8, src9, src10);
    src7 = HORIZ_8TAP_FILT(src7, src7, mask0, mask1, mask2, mask3, filt_hz0,
                           filt_hz1, filt_hz2, filt_hz3);
    tmp3 = __lsx_vpackev_b(src7, src6);
    out0 = FILT_8TAP_DPADD_S_H(tmp0, tmp1, tmp2, tmp3, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    src8 = HORIZ_8TAP_FILT(src8, src8, mask0, mask1, mask2, mask3, filt_hz0,
                           filt_hz1, filt_hz2, filt_hz3);
    src0 = __lsx_vpackev_b(src8, src7);
    out1 = FILT_8TAP_DPADD_S_H(tmp4, tmp5, tmp6, src0, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    src9 = HORIZ_8TAP_FILT(src9, src9, mask0, mask1, mask2, mask3, filt_hz0,
                           filt_hz1, filt_hz2, filt_hz3);
    src1 = __lsx_vpackev_b(src9, src8);
    src3 = FILT_8TAP_DPADD_S_H(tmp1, tmp2, tmp3, src1, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    src10 = HORIZ_8TAP_FILT(src10, src10, mask0, mask1, mask2, mask3, filt_hz0,
                            filt_hz1, filt_hz2, filt_hz3);
    src2 = __lsx_vpackev_b(src10, src9);
    src4 = FILT_8TAP_DPADD_S_H(tmp5, tmp6, src0, src2, filt_vt0, filt_vt1,
                               filt_vt2, filt_vt3);
    DUP2_ARG3(__lsx_vssrarni_b_h, out1, out0, 7, src4, src3, 7, out0, out1);
    DUP2_ARG2(__lsx_vxori_b, out0, 128, out1, 128, out0, out1);
    __lsx_vstelm_d(out0, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out0, dst, 0, 1);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 1);
    dst += dst_stride;

    src6 = src10;
    tmp0 = tmp2;
    tmp1 = tmp3;
    tmp2 = src1;
    tmp4 = tmp6;
    tmp5 = src0;
    tmp6 = src2;
  }
}

static void common_hv_8ht_8vt_16w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  common_hv_8ht_8vt_8w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                           filter_vert, height);
  src += 8;
  dst += 8;

  common_hv_8ht_8vt_8w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                           filter_vert, height);
  src += 8;
  dst += 8;
}

static void common_hv_8ht_8vt_32w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  int32_t multiple8_cnt;
  for (multiple8_cnt = 4; multiple8_cnt--;) {
    common_hv_8ht_8vt_8w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                             filter_vert, height);
    src += 8;
    dst += 8;
  }
}

static void common_hv_8ht_8vt_64w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  int32_t multiple8_cnt;
  for (multiple8_cnt = 8; multiple8_cnt--;) {
    common_hv_8ht_8vt_8w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                             filter_vert, height);
    src += 8;
    dst += 8;
  }
}

static void common_hv_2ht_2vt_4x4_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz,
                                      int8_t *filter_vert) {
  __m128i src0, src1, src2, src3, src4, mask;
  __m128i filt_vt, filt_hz, vec0, vec1, res0, res1;
  __m128i hz_out0, hz_out1, hz_out2, hz_out3, hz_out4, tmp0, tmp1;
  __m128i shuff = { 0x0F0E0D0C0B0A0908, 0x1716151413121110 };

  int32_t src_stride2 = src_stride << 1;
  int32_t src_stride3 = src_stride + src_stride2;
  int32_t src_stride4 = src_stride2 << 1;

  int32_t dst_stride2 = dst_stride << 1;
  int32_t dst_stride3 = dst_stride2 + dst_stride;
  mask = __lsx_vld(mc_filt_mask_arr, 16);

  /* rearranging filter */
  filt_hz = __lsx_vldrepl_h(filter_horiz, 0);
  filt_vt = __lsx_vldrepl_h(filter_vert, 0);

  src0 = __lsx_vld(src, 0);
  DUP4_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src, src_stride3,
            src, src_stride4, src1, src2, src3, src4);
  hz_out0 = HORIZ_2TAP_FILT_UH(src0, src1, mask, filt_hz, FILTER_BITS);
  hz_out2 = HORIZ_2TAP_FILT_UH(src2, src3, mask, filt_hz, FILTER_BITS);
  hz_out4 = HORIZ_2TAP_FILT_UH(src4, src4, mask, filt_hz, FILTER_BITS);

  hz_out1 = __lsx_vshuf_b(hz_out2, hz_out0, shuff);
  hz_out3 = __lsx_vpickod_d(hz_out4, hz_out2);

  DUP2_ARG2(__lsx_vpackev_b, hz_out1, hz_out0, hz_out3, hz_out2, vec0, vec1);
  DUP2_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, tmp0, tmp1);
  DUP2_ARG2(__lsx_vsrari_h, tmp0, FILTER_BITS, tmp1, FILTER_BITS, tmp0, tmp1);
  DUP2_ARG2(__lsx_vpickev_b, tmp0, tmp0, tmp1, tmp1, res0, res1);

  __lsx_vstelm_w(res0, dst, 0, 0);
  __lsx_vstelm_w(res0, dst + dst_stride, 0, 1);
  __lsx_vstelm_w(res1, dst + dst_stride2, 0, 0);
  __lsx_vstelm_w(res1, dst + dst_stride3, 0, 1);
}

static void common_hv_2ht_2vt_4x8_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz,
                                      int8_t *filter_vert) {
  __m128i src0, src1, src2, src3, src4, src5, src6, src7, src8, mask;
  __m128i res0, res1, res2, res3;
  __m128i filt_hz, filt_vt, vec0, vec1, vec2, vec3;
  __m128i hz_out0, hz_out1, hz_out2, hz_out3, hz_out4, hz_out5, hz_out6;
  __m128i hz_out7, hz_out8, vec4, vec5, vec6, vec7;
  __m128i shuff = { 0x0F0E0D0C0B0A0908, 0x1716151413121110 };

  int32_t src_stride2 = src_stride << 1;
  int32_t src_stride3 = src_stride2 + src_stride;
  int32_t src_stride4 = src_stride2 << 1;

  int32_t dst_stride2 = dst_stride << 1;
  int32_t dst_stride3 = dst_stride2 + dst_stride;
  int32_t dst_stride4 = dst_stride2 << 1;

  mask = __lsx_vld(mc_filt_mask_arr, 16);

  /* rearranging filter */
  DUP2_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_vert, 0, filt_hz, filt_vt);

  src0 = __lsx_vld(src, 0);
  DUP4_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src, src_stride3,
            src, src_stride4, src1, src2, src3, src4);
  src += src_stride4;
  DUP4_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src, src_stride3,
            src, src_stride4, src5, src6, src7, src8);
  src += src_stride4;

  hz_out0 = HORIZ_2TAP_FILT_UH(src0, src1, mask, filt_hz, FILTER_BITS);
  hz_out2 = HORIZ_2TAP_FILT_UH(src2, src3, mask, filt_hz, FILTER_BITS);
  hz_out4 = HORIZ_2TAP_FILT_UH(src4, src5, mask, filt_hz, FILTER_BITS);
  hz_out6 = HORIZ_2TAP_FILT_UH(src6, src7, mask, filt_hz, FILTER_BITS);
  hz_out8 = HORIZ_2TAP_FILT_UH(src8, src8, mask, filt_hz, FILTER_BITS);

  DUP2_ARG3(__lsx_vshuf_b, hz_out2, hz_out0, shuff, hz_out4, hz_out2, shuff,
            hz_out1, hz_out3);
  hz_out5 = __lsx_vshuf_b(hz_out6, hz_out4, shuff);
  hz_out7 = __lsx_vpickod_d(hz_out8, hz_out6);
  DUP4_ARG2(__lsx_vpackev_b, hz_out1, hz_out0, hz_out3, hz_out2, hz_out5,
            hz_out4, hz_out7, hz_out6, vec0, vec1, vec2, vec3);
  DUP4_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, vec2, filt_vt, vec3,
            filt_vt, vec4, vec5, vec6, vec7);
  DUP4_ARG2(__lsx_vsrari_h, vec4, FILTER_BITS, vec5, FILTER_BITS, vec6,
            FILTER_BITS, vec7, FILTER_BITS, vec4, vec5, vec6, vec7);
  DUP4_ARG2(__lsx_vpickev_b, vec4, vec4, vec5, vec5, vec6, vec6, vec7, vec7,
            res0, res1, res2, res3);

  __lsx_vstelm_w(res0, dst, 0, 0);
  __lsx_vstelm_w(res0, dst + dst_stride, 0, 1);
  __lsx_vstelm_w(res1, dst + dst_stride2, 0, 0);
  __lsx_vstelm_w(res1, dst + dst_stride3, 0, 1);
  dst += dst_stride4;
  __lsx_vstelm_w(res2, dst, 0, 0);
  __lsx_vstelm_w(res2, dst + dst_stride, 0, 1);
  __lsx_vstelm_w(res3, dst + dst_stride2, 0, 0);
  __lsx_vstelm_w(res3, dst + dst_stride3, 0, 1);
}

static void common_hv_2ht_2vt_4w_lsx(const uint8_t *src, int32_t src_stride,
                                     uint8_t *dst, int32_t dst_stride,
                                     int8_t *filter_horiz, int8_t *filter_vert,
                                     int32_t height) {
  if (height == 4) {
    common_hv_2ht_2vt_4x4_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                              filter_vert);
  } else if (height == 8) {
    common_hv_2ht_2vt_4x8_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                              filter_vert);
  }
}

static void common_hv_2ht_2vt_8x4_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz,
                                      int8_t *filter_vert) {
  __m128i src0, src1, src2, src3, src4, mask, out0, out1;
  __m128i filt_hz, filt_vt, vec0, vec1, vec2, vec3;
  __m128i hz_out0, hz_out1, tmp0, tmp1, tmp2, tmp3;

  int32_t src_stride2 = src_stride << 1;
  int32_t src_stride3 = src_stride2 + src_stride;
  int32_t src_stride4 = src_stride2 << 1;

  int32_t dst_stride2 = dst_stride << 1;
  int32_t dst_stride3 = dst_stride2 + dst_stride;

  mask = __lsx_vld(mc_filt_mask_arr, 0);

  /* rearranging filter */
  DUP2_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_vert, 0, filt_hz, filt_vt);

  src0 = __lsx_vld(src, 0);
  DUP4_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src, src_stride3,
            src, src_stride4, src1, src2, src3, src4);

  hz_out0 = HORIZ_2TAP_FILT_UH(src0, src0, mask, filt_hz, FILTER_BITS);
  hz_out1 = HORIZ_2TAP_FILT_UH(src1, src1, mask, filt_hz, FILTER_BITS);
  vec0 = __lsx_vpackev_b(hz_out1, hz_out0);
  tmp0 = __lsx_vdp2_h_bu(vec0, filt_vt);

  hz_out0 = HORIZ_2TAP_FILT_UH(src2, src2, mask, filt_hz, FILTER_BITS);
  vec1 = __lsx_vpackev_b(hz_out0, hz_out1);
  tmp1 = __lsx_vdp2_h_bu(vec1, filt_vt);

  hz_out1 = HORIZ_2TAP_FILT_UH(src3, src3, mask, filt_hz, FILTER_BITS);
  vec2 = __lsx_vpackev_b(hz_out1, hz_out0);
  tmp2 = __lsx_vdp2_h_bu(vec2, filt_vt);

  hz_out0 = HORIZ_2TAP_FILT_UH(src4, src4, mask, filt_hz, FILTER_BITS);
  vec3 = __lsx_vpackev_b(hz_out0, hz_out1);
  tmp3 = __lsx_vdp2_h_bu(vec3, filt_vt);

  DUP4_ARG2(__lsx_vsrari_h, tmp0, FILTER_BITS, tmp1, FILTER_BITS, tmp2,
            FILTER_BITS, tmp3, FILTER_BITS, tmp0, tmp1, tmp2, tmp3);
  DUP2_ARG2(__lsx_vpickev_b, tmp1, tmp0, tmp3, tmp2, out0, out1);

  __lsx_vstelm_d(out0, dst, 0, 0);
  __lsx_vstelm_d(out0, dst + dst_stride, 0, 1);
  __lsx_vstelm_d(out1, dst + dst_stride2, 0, 0);
  __lsx_vstelm_d(out1, dst + dst_stride3, 0, 1);
}

static void common_hv_2ht_2vt_8x8mult_lsx(const uint8_t *src,
                                          int32_t src_stride, uint8_t *dst,
                                          int32_t dst_stride,
                                          int8_t *filter_horiz,
                                          int8_t *filter_vert, int32_t height) {
  uint32_t loop_cnt = (height >> 3);
  __m128i src0, src1, src2, src3, src4, mask, out0, out1;
  __m128i filt_hz, filt_vt, vec0;
  __m128i hz_out0, hz_out1, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7, tmp8;

  int32_t src_stride2 = src_stride << 1;
  int32_t src_stride3 = src_stride2 + src_stride;
  int32_t src_stride4 = src_stride2 << 1;

  mask = __lsx_vld(mc_filt_mask_arr, 0);

  /* rearranging filter */
  DUP2_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_vert, 0, filt_hz, filt_vt);

  src0 = __lsx_vld(src, 0);
  src += src_stride;

  hz_out0 = HORIZ_2TAP_FILT_UH(src0, src0, mask, filt_hz, FILTER_BITS);

  for (; loop_cnt--;) {
    src1 = __lsx_vld(src, 0);
    DUP2_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src2, src3);
    src4 = __lsx_vldx(src, src_stride3);
    src += src_stride4;

    hz_out1 = HORIZ_2TAP_FILT_UH(src1, src1, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out1, hz_out0);
    tmp1 = __lsx_vdp2_h_bu(vec0, filt_vt);

    hz_out0 = HORIZ_2TAP_FILT_UH(src2, src2, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out0, hz_out1);
    tmp2 = __lsx_vdp2_h_bu(vec0, filt_vt);

    DUP2_ARG2(__lsx_vsrari_h, tmp1, FILTER_BITS, tmp2, FILTER_BITS, tmp1, tmp2);

    hz_out1 = HORIZ_2TAP_FILT_UH(src3, src3, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out1, hz_out0);
    tmp3 = __lsx_vdp2_h_bu(vec0, filt_vt);

    hz_out0 = HORIZ_2TAP_FILT_UH(src4, src4, mask, filt_hz, FILTER_BITS);
    src1 = __lsx_vld(src, 0);
    DUP2_ARG2(__lsx_vldx, src, src_stride, src, src_stride2, src2, src3);
    src4 = __lsx_vldx(src, src_stride3);
    src += src_stride4;
    vec0 = __lsx_vpackev_b(hz_out0, hz_out1);
    tmp4 = __lsx_vdp2_h_bu(vec0, filt_vt);

    DUP2_ARG2(__lsx_vsrari_h, tmp3, FILTER_BITS, tmp4, FILTER_BITS, tmp3, tmp4);
    DUP2_ARG2(__lsx_vpickev_b, tmp2, tmp1, tmp4, tmp3, out0, out1);
    __lsx_vstelm_d(out0, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out0, dst, 0, 1);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 1);
    dst += dst_stride;

    hz_out1 = HORIZ_2TAP_FILT_UH(src1, src1, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out1, hz_out0);
    tmp5 = __lsx_vdp2_h_bu(vec0, filt_vt);

    hz_out0 = HORIZ_2TAP_FILT_UH(src2, src2, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out0, hz_out1);
    tmp6 = __lsx_vdp2_h_bu(vec0, filt_vt);

    hz_out1 = HORIZ_2TAP_FILT_UH(src3, src3, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out1, hz_out0);
    tmp7 = __lsx_vdp2_h_bu(vec0, filt_vt);

    hz_out0 = HORIZ_2TAP_FILT_UH(src4, src4, mask, filt_hz, FILTER_BITS);
    vec0 = __lsx_vpackev_b(hz_out0, hz_out1);
    tmp8 = __lsx_vdp2_h_bu(vec0, filt_vt);

    DUP4_ARG2(__lsx_vsrari_h, tmp5, FILTER_BITS, tmp6, FILTER_BITS, tmp7,
              FILTER_BITS, tmp8, FILTER_BITS, tmp5, tmp6, tmp7, tmp8);
    DUP2_ARG2(__lsx_vpickev_b, tmp6, tmp5, tmp8, tmp7, out0, out1);
    __lsx_vstelm_d(out0, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out0, dst, 0, 1);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 0);
    dst += dst_stride;
    __lsx_vstelm_d(out1, dst, 0, 1);
    dst += dst_stride;
  }
}

static void common_hv_2ht_2vt_8w_lsx(const uint8_t *src, int32_t src_stride,
                                     uint8_t *dst, int32_t dst_stride,
                                     int8_t *filter_horiz, int8_t *filter_vert,
                                     int32_t height) {
  if (height == 4) {
    common_hv_2ht_2vt_8x4_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                              filter_vert);
  } else {
    common_hv_2ht_2vt_8x8mult_lsx(src, src_stride, dst, dst_stride,
                                  filter_horiz, filter_vert, height);
  }
}

static void common_hv_2ht_2vt_16w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  uint32_t loop_cnt = (height >> 2);
  __m128i src0, src1, src2, src3, src4, src5, src6, src7, mask;
  __m128i filt_hz, filt_vt, vec0, vec1;
  __m128i tmp, tmp1, tmp2, hz_out0, hz_out1, hz_out2, hz_out3;

  int32_t src_stride2 = src_stride << 1;
  int32_t src_stride3 = src_stride2 + src_stride;
  int32_t src_stride4 = src_stride2 << 1;

  mask = __lsx_vld(mc_filt_mask_arr, 0);

  /* rearranging filter */
  DUP2_ARG2(__lsx_vldrepl_h, filter_horiz, 0, filter_vert, 0, filt_hz, filt_vt);

  DUP2_ARG2(__lsx_vld, src, 0, src, 8, src0, src1);
  src += src_stride;

  hz_out0 = HORIZ_2TAP_FILT_UH(src0, src0, mask, filt_hz, FILTER_BITS);
  hz_out2 = HORIZ_2TAP_FILT_UH(src1, src1, mask, filt_hz, FILTER_BITS);

  for (; loop_cnt--;) {
    uint8_t *src_tmp0 = src + 8;

    DUP2_ARG2(__lsx_vld, src, 0, src_tmp0, 0, src0, src1);
    DUP4_ARG2(__lsx_vldx, src, src_stride, src_tmp0, src_stride, src,
              src_stride2, src_tmp0, src_stride2, src2, src3, src4, src5);
    DUP2_ARG2(__lsx_vldx, src, src_stride3, src_tmp0, src_stride3, src6, src7);
    src += src_stride4;

    hz_out1 = HORIZ_2TAP_FILT_UH(src0, src0, mask, filt_hz, FILTER_BITS);
    hz_out3 = HORIZ_2TAP_FILT_UH(src1, src1, mask, filt_hz, FILTER_BITS);
    DUP2_ARG2(__lsx_vpackev_b, hz_out1, hz_out0, hz_out3, hz_out2, vec0, vec1);
    DUP2_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, tmp1, tmp2);
    DUP2_ARG2(__lsx_vsrari_h, tmp1, FILTER_BITS, tmp2, FILTER_BITS, tmp1, tmp2);
    tmp = __lsx_vpickev_b(tmp2, tmp1);
    __lsx_vst(tmp, dst, 0);
    dst += dst_stride;

    hz_out0 = HORIZ_2TAP_FILT_UH(src2, src2, mask, filt_hz, FILTER_BITS);
    hz_out2 = HORIZ_2TAP_FILT_UH(src3, src3, mask, filt_hz, FILTER_BITS);
    DUP2_ARG2(__lsx_vpackev_b, hz_out0, hz_out1, hz_out2, hz_out3, vec0, vec1);
    DUP2_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, tmp1, tmp2);
    DUP2_ARG2(__lsx_vsrari_h, tmp1, FILTER_BITS, tmp2, FILTER_BITS, tmp1, tmp2);
    tmp = __lsx_vpickev_b(tmp2, tmp1);
    __lsx_vst(tmp, dst, 0);
    dst += dst_stride;

    hz_out1 = HORIZ_2TAP_FILT_UH(src4, src4, mask, filt_hz, FILTER_BITS);
    hz_out3 = HORIZ_2TAP_FILT_UH(src5, src5, mask, filt_hz, FILTER_BITS);
    DUP2_ARG2(__lsx_vpackev_b, hz_out1, hz_out0, hz_out3, hz_out2, vec0, vec1);
    DUP2_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, tmp1, tmp2);
    DUP2_ARG2(__lsx_vsrari_h, tmp1, FILTER_BITS, tmp2, FILTER_BITS, tmp1, tmp2);
    tmp = __lsx_vpickev_b(tmp2, tmp1);
    __lsx_vst(tmp, dst, 0);
    dst += dst_stride;

    hz_out0 = HORIZ_2TAP_FILT_UH(src6, src6, mask, filt_hz, FILTER_BITS);
    hz_out2 = HORIZ_2TAP_FILT_UH(src7, src7, mask, filt_hz, FILTER_BITS);
    DUP2_ARG2(__lsx_vpackev_b, hz_out0, hz_out1, hz_out2, hz_out3, vec0, vec1);
    DUP2_ARG2(__lsx_vdp2_h_bu, vec0, filt_vt, vec1, filt_vt, tmp1, tmp2);
    DUP2_ARG2(__lsx_vsrari_h, tmp1, FILTER_BITS, tmp2, FILTER_BITS, tmp1, tmp2);
    tmp = __lsx_vpickev_b(tmp2, tmp1);
    __lsx_vst(tmp, dst, 0);
    dst += dst_stride;
  }
}

static void common_hv_2ht_2vt_32w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  common_hv_2ht_2vt_16w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                            filter_vert, height);
  src += 16;
  dst += 16;

  common_hv_2ht_2vt_16w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                            filter_vert, height);
  src += 16;
  dst += 16;
}

static void common_hv_2ht_2vt_64w_lsx(const uint8_t *src, int32_t src_stride,
                                      uint8_t *dst, int32_t dst_stride,
                                      int8_t *filter_horiz, int8_t *filter_vert,
                                      int32_t height) {
  int32_t multiple8_cnt;
  for (multiple8_cnt = 4; multiple8_cnt--;) {
    common_hv_2ht_2vt_16w_lsx(src, src_stride, dst, dst_stride, filter_horiz,
                              filter_vert, height);
    src += 16;
    dst += 16;
  }
}

void vpx_convolve8_lsx(const uint8_t *src, ptrdiff_t src_stride, uint8_t *dst,
                       ptrdiff_t dst_stride, const InterpKernel *filter,
                       int x0_q4, int32_t x_step_q4, int y0_q4,
                       int32_t y_step_q4, int32_t w, int32_t h) {
  const int16_t *const filter_x = filter[x0_q4];
  const int16_t *const filter_y = filter[y0_q4];
  int8_t cnt, filt_hor[8], filt_ver[8];

  assert(x_step_q4 == 16);
  assert(y_step_q4 == 16);
  assert(((const int32_t *)filter_x)[1] != 0x800000);
  assert(((const int32_t *)filter_y)[1] != 0x800000);

  for (cnt = 0; cnt < 8; ++cnt) {
    filt_hor[cnt] = filter_x[cnt];
    filt_ver[cnt] = filter_y[cnt];
  }

  if (vpx_get_filter_taps(filter_x) == 2 &&
      vpx_get_filter_taps(filter_y) == 2) {
    switch (w) {
      case 4:
        common_hv_2ht_2vt_4w_lsx(src, (int32_t)src_stride, dst,
                                 (int32_t)dst_stride, &filt_hor[3],
                                 &filt_ver[3], (int32_t)h);
        break;
      case 8:
        common_hv_2ht_2vt_8w_lsx(src, (int32_t)src_stride, dst,
                                 (int32_t)dst_stride, &filt_hor[3],
                                 &filt_ver[3], (int32_t)h);
        break;
      case 16:
        common_hv_2ht_2vt_16w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, &filt_hor[3],
                                  &filt_ver[3], (int32_t)h);
        break;
      case 32:
        common_hv_2ht_2vt_32w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, &filt_hor[3],
                                  &filt_ver[3], (int32_t)h);
        break;
      case 64:
        common_hv_2ht_2vt_64w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, &filt_hor[3],
                                  &filt_ver[3], (int32_t)h);
        break;
      default:
        vpx_convolve8_c(src, src_stride, dst, dst_stride, filter, x0_q4,
                        x_step_q4, y0_q4, y_step_q4, w, h);
        break;
    }
  } else if (vpx_get_filter_taps(filter_x) == 2 ||
             vpx_get_filter_taps(filter_y) == 2) {
    vpx_convolve8_c(src, src_stride, dst, dst_stride, filter, x0_q4, x_step_q4,
                    y0_q4, y_step_q4, w, h);
  } else {
    switch (w) {
      case 4:
        common_hv_8ht_8vt_4w_lsx(src, (int32_t)src_stride, dst,
                                 (int32_t)dst_stride, filt_hor, filt_ver,
                                 (int32_t)h);
        break;
      case 8:
        common_hv_8ht_8vt_8w_lsx(src, (int32_t)src_stride, dst,
                                 (int32_t)dst_stride, filt_hor, filt_ver,
                                 (int32_t)h);
        break;
      case 16:
        common_hv_8ht_8vt_16w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, filt_hor, filt_ver,
                                  (int32_t)h);
        break;
      case 32:
        common_hv_8ht_8vt_32w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, filt_hor, filt_ver,
                                  (int32_t)h);
        break;
      case 64:
        common_hv_8ht_8vt_64w_lsx(src, (int32_t)src_stride, dst,
                                  (int32_t)dst_stride, filt_hor, filt_ver,
                                  (int32_t)h);
        break;
      default:
        vpx_convolve8_c(src, src_stride, dst, dst_stride, filter, x0_q4,
                        x_step_q4, y0_q4, y_step_q4, w, h);
        break;
    }
  }
}
