export default class Platform {
    static LINUX = {
        KEY: 'linux',
        NAME: 'Linux',
        ICON: 'linux',
        INSTALLER_EXT: 'deb',
    };

    static MACOS = {
        KEY: 'macosx',
        NAME: 'Mac OS',
        ICON: 'apple',
        INSTALLER_EXT: 'dmg',
    };

    static WINDOWS = {
        KEY: 'windows',
        NAME: 'Windows',
        ICON: 'windows',
        INSTALLER_EXT: 'exe',
    }

    static CURRENT;

    static staticInitializer = function () {
        let platform = window.navigator.platform.toLocaleLowerCase();
        if (platform.startsWith('lin')) {
            Platform.CURRENT = Platform.LINUX;
        } else if (platform.startsWith('mac')) {
            Platform.CURRENT = Platform.MACOS;
        } else if (platform.startsWith('win')) {
            Platform.CURRENT = Platform.WINDOWS;
        }
    }()
}
