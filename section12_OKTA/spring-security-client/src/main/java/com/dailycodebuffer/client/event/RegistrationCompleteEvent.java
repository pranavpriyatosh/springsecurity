package com.dailycodebuffer.client.event;

import com.dailycodebuffer.client.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private final User user;
    private final String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getApplicationUrl() {
		// TODO Auto-generated method stub
		return null;
	}
}
