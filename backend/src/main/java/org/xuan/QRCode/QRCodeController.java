package org.xuan.QRCode;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成二维码
 * 参考：https://mp.weixin.qq.com/s/F4JibVft-0eVM7wPJbHDrg
 * @author 醒酒器
 */
@RestController
@RequestMapping("/qr")
public class QRCodeController {

    @GetMapping("/generateQRCode")
    public ResponseEntity<Resource> generateQRCode(@RequestParam String text, @RequestParam Integer width, @RequestParam Integer height) throws IOException, WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, getHints());

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "PNG", byteArrayOutputStream);

        byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(qrCodeBytes.length)
                .body(new ByteArrayResource(qrCodeBytes));
    }

    @GetMapping(value = "/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode)
            throws Exception {

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcode, BarcodeFormat.QR_CODE, 200, 200);

        return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix), HttpStatus.OK);
    }

    private Map<EncodeHintType, Object> getHints() {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        return hints;
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}

