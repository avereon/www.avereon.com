export class Platform {

	static PLATFORM = 'unknown';

	static NAME = 'Unknown';

}

console.log(window.navigator);

let platform = window.navigator.platform.toLocaleLowerCase();
if (platform.startsWith('linux')) {
	Platform.PLATFORM = 'linux';
	Platform.NAME = 'Linux';
} else if (platform.startsWith('win')) {
	Platform.PLATFORM = 'windows';
	Platform.NAME = 'Windows';
} else if (platform.startsWith('mac')) {
	Platform.PLATFORM = 'macosx';
	Platform.NAME = 'Mac OS'
}
