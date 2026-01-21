package com.example.onbid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.onbid.service.AuthService;
import com.example.onbid.service.PropertyService;
import com.example.onbid.service.WishlistService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OnbidController {

    private final PropertyService propertyService;
    private final AuthService authService;
    private final WishlistService wishlistService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/onbid")
    public String onbidPage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("isLoggedIn", username != null);
        if (username != null) {
            model.addAttribute("userWishlist", wishlistService.getUserWishlist(username));
        }
        return propertyService.processOnbidPage(model);
    }
    
    @GetMapping("/refresh")
    public String refreshData(RedirectAttributes redirectAttributes) {
        return propertyService.processRefresh(redirectAttributes);
    }
    
    @GetMapping("/detail/{propertyId}")
    public String propertyDetail(@PathVariable("propertyId") String propertyId, Model model) {
        return propertyService.processDetail(propertyId, model);
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, 
                       HttpSession session, RedirectAttributes redirectAttributes) {
        return authService.processLogin(username, password, session, redirectAttributes);
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, 
                          RedirectAttributes redirectAttributes) {
        return authService.processRegister(username, password, redirectAttributes);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        return authService.processLogout(session);
    }
    
    @PostMapping("/wishlist/add")
    @ResponseBody
    public String addWishlist(@RequestParam("propertyId") String propertyId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return wishlistService.processAddWishlist(propertyId, username);
    }
    
    @PostMapping("/wishlist/remove")
    @ResponseBody
    public String removeWishlist(@RequestParam("propertyId") String propertyId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return wishlistService.processRemoveWishlist(propertyId, username);
    }
    
    @GetMapping("/wishlist")
    public String wishlistPage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        return wishlistService.processWishlistPage(model, username);
    }

}