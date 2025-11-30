package com.yxx.ai.manager;

import com.yxx.ai.domain.DocumentInfoDTO;
import com.yxx.common.utils.StringUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class VectorDocumentManager {

    private final VectorStore vectorStore;

    public VectorDocumentManager(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public DocumentInfoDTO writeToVectorStore(Resource resource) {
        // 创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 写入向量库
        vectorStore.add(documents);
        AtomicInteger length = new AtomicInteger();
        documents.forEach(i -> {
            String text = i.getText();
            if (StringUtils.isNotEmpty(text)) {
                length.addAndGet(text.length());
            }
        });
        return new DocumentInfoDTO().setSize(documents.size()).setContentLength(length.get());
    }
}
