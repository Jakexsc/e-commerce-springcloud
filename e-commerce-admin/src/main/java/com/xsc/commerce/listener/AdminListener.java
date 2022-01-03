package com.xsc.commerce.listener;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Jakexsc
 * 2021/12/26
 */
@Component
@Slf4j
public class AdminListener extends AbstractEventNotifier {

    protected AdminListener(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
           if (event instanceof InstanceStatusChangedEvent) {
               log.warn("Instance status change [{}] [{}] [{}]",
                       instance.getRegistration().getName(),
                       event.getInstance(),
                       ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus()
               );
           } else {
               log.warn("Instance status change [{}] [{}] [{}]",
                       instance.getRegistration().getName(),
                       event.getInstance(),
                       event.getType()
               );

           }
        });
    }
}
