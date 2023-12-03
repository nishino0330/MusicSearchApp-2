package com.example.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpotifyController {

    // SpotifyServiceをDIする
    @Autowired
    private SpotifyService spotifyService;

    // 楽曲検索ページのマッピング
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 楽曲検索の処理
    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        // SpotifyServiceのsearchTracksメソッドを呼び出して楽曲の情報を取得する
        Map<String, Object> tracks = spotifyService.searchTracks(query);

        // モデルに楽曲の情報を設定する
        model.addAttribute("tracks", tracks);

        // 検索結果ページを返す
        return "result";
    }
}