package com.appclientside.data;

import android.content.Context;

import com.appclientside.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, Context ctx) {


        LoggedInUser fakeUser =
                new LoggedInUser(
                        java.util.UUID.randomUUID().toString(),
                        "Jane Doe");
        return new Result.Success<>(fakeUser);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
