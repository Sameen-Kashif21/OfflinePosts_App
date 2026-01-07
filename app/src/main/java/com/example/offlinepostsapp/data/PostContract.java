package com.example.offlinepostsapp.data;

import android.provider.BaseColumns;

public class PostContract {
    public static class PostEntry implements BaseColumns {
        public static final String TABLE = "posts";
        public static final String COL_POST_ID = "post_id";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_TITLE = "title";
        public static final String COL_BODY = "body";
    }
}
