import {notification} from "antd";

const openNotificationWithIcon = (type, message, description, duration) => {
    notification[type]({message, description, duration});
};

export const successNotification = (message, description, duration) =>
    openNotificationWithIcon('success', message, description, duration);

export const errorNotification = (message, description, duration) =>
    openNotificationWithIcon('error', message, description, duration);

export const infoNotification = (message, description, duration) =>
    openNotificationWithIcon('info', message, description, duration);

export const warningNotification = (message, description, duration) =>
    openNotificationWithIcon('warning', message, description, duration);