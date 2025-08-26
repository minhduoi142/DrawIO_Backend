    package com.Minh.DrIO.DTO.Request.Response;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    @Data
    @NoArgsConstructor
    public class GuestUserResponse {
        public GuestUserResponse(String idToken, String nickname)
        {
            this.idToken = idToken;
            this.nickname = nickname;
        }
        private String idToken;
        private String nickname;
    }
