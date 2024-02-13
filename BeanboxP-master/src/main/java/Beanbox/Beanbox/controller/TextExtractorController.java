package Beanbox.Beanbox.controller;

import Beanbox.Beanbox.dto.BeanDto;
import Beanbox.Beanbox.model.BeanMapper;
import Beanbox.Beanbox.service.BeanService;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TextExtractorController {
    @Autowired
    public BeanService beanService;

    @GetMapping("/uploadpage")
    public String home() {
        return "uploadpage";
    }

    @PostMapping("/upload")
    public String extractText(@RequestParam("file") MultipartFile file, Model model) {
        ImageAnnotatorClient client = null;
        try {
            // MultipartFile을 BufferedImage로 변환
            BufferedImage image = ImageIO.read(file.getInputStream());

            // 이미지 크기 조정
            int maxWidth = 800; // 최대 너비
            int maxHeight = 600; // 최대 높이
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            int newWidth = originalWidth;
            int newHeight = originalHeight;

            // 이미지가 최대 크기를 초과하는 경우에만 크기 조정
            if (originalWidth > maxWidth || originalHeight > maxHeight) {
                // 비율 유지하며 적절한 크기로 조정
                if (originalWidth > originalHeight) {
                    newWidth = maxWidth;
                    newHeight = (int) Math.round((double) originalHeight / originalWidth * maxWidth);
                } else {
                    newHeight = maxHeight;
                    newWidth = (int) Math.round((double) originalWidth / originalHeight * maxHeight);
                }
            }

            // 이미지 크기 조정
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            resizedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

            // 압축된 이미지를 byte 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            byte[] compressedImageBytes = baos.toByteArray();

            // byte 배열을 이용하여 Image 생성
            ByteString imgBytes = ByteString.copyFrom(compressedImageBytes);
            com.google.cloud.vision.v1.Image visionImage = com.google.cloud.vision.v1.Image.newBuilder().setContent(imgBytes).build();

            // 구글 비전 API를 사용하여 이미지에서 텍스트 추출
            client = ImageAnnotatorClient.create(); // ImageAnnotatorClient 인스턴스 생성

            // API 요청 객체 생성
            List<AnnotateImageRequest> requests = new ArrayList<>();
            ImageContext imageContext = ImageContext.newBuilder().addLanguageHints("ko").build();
            Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(visionImage)
                    .setImageContext(imageContext)
                    .build();
            requests.add(request);

            // API 호출 및 응답 받기
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // 추출한 텍스트 가져오기
            StringBuilder extractedText = new StringBuilder();
            for (AnnotateImageResponse annotateResponse : responses) {
                if (annotateResponse.hasError()) {
                    System.err.println("Error: " + annotateResponse.getError().getMessage());
                    return "error"; // error handling
                }
                for (EntityAnnotation annotation : annotateResponse.getTextAnnotationsList()) {
                    extractedText.append(annotation.getDescription().replaceAll("[\\s\\p{P}]", "").toUpperCase());
                }
            }

            // 필터링된 결과를 담을 새로운 리스트 생성
            List<BeanDto> filteredBeans = new ArrayList<>();

            // beanList에서 조건을 확인하여 필터링
            List<BeanDto> beanList = beanService.getBeanList();
            for (BeanDto bean : beanList) {
                filteredBeans.add(bean);
            }

            // 모델에 필터링된 결과 추가
            model.addAttribute("filteredBeans", filteredBeans);
            // 모델에 추출한 텍스트, 공백 제거한 텍스트 추가
            model.addAttribute("extractedText", extractedText.toString());

            System.out.println(filteredBeans);
            System.out.println(extractedText);

            return "result"; // 결과 페이지로 이동
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } finally {
            // 리소스 해제
            if (client != null) {
                client.close();
            }
        }
    }

    @GetMapping("/result")
    public String result(@RequestParam("beanName") String beanName, Model model) {
        List<BeanDto> filteredBeans = new ArrayList<>();
        String extractedText = beanName;

        // beanList에서 조건을 확인하여 필터링
        List<BeanDto> beanList = beanService.getBeanList();
        for (BeanDto bean : beanList) {
            filteredBeans.add(bean);
        }

        // 모델에 필터링된 결과 추가
        model.addAttribute("filteredBeans", filteredBeans);
        // 모델에 추출한 텍스트, 공백 제거한 텍스트 추가
        model.addAttribute("extractedText", extractedText);


        return "result";
    }
}