export class Platform {

	static KEY = 'unknown';

	static NAME = 'Unknown';

	static ICON = '';

	static LINUX = {
		KEY: 'linux',
		NAME: 'Linux',
		ICON: 'linux',
	};

	static MAC = {
		KEY: 'macosx',
		NAME: 'Mac OS',
		ICON: 'apple',
	};

	static WINDOWS = {
		KEY: 'windows',
		NAME: 'Windows',
		ICON: 'windows',
	}

}

console.log(window.navigator);

let platform = window.navigator.platform.toLocaleLowerCase();
if (platform.startsWith('linux')) {
	Platform.KEY = Platform.LINUX.KEY;
	Platform.NAME = Platform.LINUX.NAME;
} else if (platform.startsWith('win')) {
	Platform.KEY = Platform.WINDOWS.KEY;
	Platform.NAME = Platform.WINDOWS.NAME;
} else if (platform.startsWith('mac')) {
	Platform.KEY = Platform.MAC.KEY;
	Platform.NAME = Platform.MAC.NAME;
}
