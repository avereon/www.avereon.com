export class Platform {

	static KEY = 'unknown';

	static NAME = 'Unknown';

}

console.log(window.navigator);

let platform = window.navigator.platform.toLocaleLowerCase();
if (platform.startsWith('linux')) {
	Platform.KEY = 'linux';
	Platform.NAME = 'Linux';
} else if (platform.startsWith('win')) {
	Platform.KEY = 'windows';
	Platform.NAME = 'Windows';
} else if (platform.startsWith('mac')) {
	Platform.KEY = 'macosx';
	Platform.NAME = 'Mac OS'
}
