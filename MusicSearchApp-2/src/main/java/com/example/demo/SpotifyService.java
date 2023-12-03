package com.example.demo;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    // RestTemplateをフィールドとして宣言する
    private RestTemplate restTemplate;

    // Spotify APIの認証情報をapplication.propertiesから読み込む
    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    // Spotify APIのエンドポイントをapplication.propertiesから読み込む
    @Value("${spotify.api.base}")
    private String apiBase;

    @Value("${spotify.auth.token}")
    private String authToken;

    // アクセストークンを保持する変数
    private String accessToken;

    // RestTemplateBuilderをコンストラクタで受け取ってRestTemplateを生成する
    public SpotifyService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // アクセストークンを取得するメソッド
    private void getAccessToken() {
        // 認証情報をBase64エンコードする
        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        // ヘッダーに認証情報とコンテントタイプを設定する
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + credentials);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // ボディにgrant_typeを設定する
        // HashMapではなく、MultiValueMapを使う
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        // HttpEntityにヘッダーとボディを設定する
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // POSTリクエストを送ってレスポンスを受け取る
        ResponseEntity<Map> response = restTemplate.postForEntity(authToken, request, Map.class);

        // レスポンスからアクセストークンを取得して変数に代入する
        accessToken = (String) response.getBody().get("access_token");
    }
    // 楽曲検索を行うメソッド
    public Map<String, Object> searchTracks(String query) {
        // アクセストークンがない場合は取得する
        if (accessToken == null) {
            getAccessToken();
        }

        // ヘッダーにアクセストークンを設定する
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntityにヘッダーを設定する
        HttpEntity<String> request = new HttpEntity<>(headers);

        // GETリクエストを送ってレスポンスを受け取る
        ResponseEntity<Map> response = restTemplate.exchange(apiBase + "/search?q=" + query + "&type=track", HttpMethod.GET, request, Map.class);

        // レスポンスから楽曲の情報を取得して返す
        return (Map<String, Object>) response.getBody().get("tracks");
    }
}