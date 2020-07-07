package com.tony.billing.filters.wapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwenjie 2020/7/6
 */
public class TokenServletRequestWrapperWithFile extends TokenServletRequestWrapper implements MultipartHttpServletRequest {

    private MultipartHttpServletRequest originalFileRequest;

    public TokenServletRequestWrapperWithFile(MultipartHttpServletRequest request) {
        super(request);
        this.originalFileRequest = request;
    }

    @Override
    public HttpMethod getRequestMethod() {
        return originalFileRequest.getRequestMethod();
    }

    @Override
    public HttpHeaders getRequestHeaders() {
        return originalFileRequest.getRequestHeaders();
    }

    @Override
    public HttpHeaders getMultipartHeaders(String s) {
        return originalFileRequest.getMultipartHeaders(s);
    }

    @Override
    public Iterator<String> getFileNames() {
        return originalFileRequest.getFileNames();
    }

    @Override
    public MultipartFile getFile(String s) {
        return originalFileRequest.getFile(s);
    }

    @Override
    public List<MultipartFile> getFiles(String s) {
        return originalFileRequest.getFiles(s);
    }

    @Override
    public Map<String, MultipartFile> getFileMap() {
        return originalFileRequest.getFileMap();
    }

    @Override
    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return originalFileRequest.getMultiFileMap();
    }

    @Override
    public String getMultipartContentType(String s) {
        return originalFileRequest.getMultipartContentType(s);
    }
}
