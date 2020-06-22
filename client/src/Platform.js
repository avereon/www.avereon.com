export const LINUX = {
	KEY: 'linux',
	NAME: 'Linux',
	ICON: 'linux',
	INSTALLER_EXT: 'deb',
};

export const MACOS = {
	KEY: 'macosx',
	NAME: 'Mac OS',
	ICON: 'apple',
	INSTALLER_EXT: 'dmg',
};

export const WINDOWS = {
	KEY: 'windows',
	NAME: 'Windows',
	ICON: 'windows',
	INSTALLER_EXT: 'exe',
}

let current;
let platform = window.navigator.platform.toLocaleLowerCase();
if (platform.startsWith('linux')) {
	current = LINUX;
} else if (platform.startsWith('win')) {
	current = WINDOWS;
} else if (platform.startsWith('mac')) {
	current = MACOS;
}

export const CURRENT = current;
