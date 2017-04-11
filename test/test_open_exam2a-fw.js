var co = require('co');
var expect = require('expect');
var runFramework = require('./lib.js').runFramework;
var _ = require('lodash');

describe('Exam 2A - Framework', () => {
	it('should solve a simple case and the first player should win', () => co(function* () {
		const ai1 = './ai.sh';
		const ai2 = './ai.sh';
		const start = 'shiritori';
		const words = ['internet', 'tail', 'less'];
		const expected = [
			/^FIRST \(OK\): internet$/,
			/^SECOND \(OK\): tail$/,
			/^FIRST \(OK\): less$/,
			/^SECOND \(NG\):/,
			/^WIN - FIRST$/
		];
		const actual = yield runFramework(ai1, ai2, start, words);
		_.zip(expected, actual).forEach(([e, a]) => expect(a).toMatch(e) );
	}));

	it('should solve a simple case and the second player should win', () => co(function* () {
		const ai1 = './ai.sh';
		const ai2 = './ai.sh';
		const start = 'shiritori';
		const words = ['internet', 'tail'];
		const expected = [
			/^FIRST \(OK\): internet$/,
			/^SECOND \(OK\): tail$/,
			/^FIRST \(NG\):/,
			/^WIN - SECOND$/
		];
		const actual = yield runFramework(ai1, ai2, start, words);
		_.zip(expected, actual).forEach(([e, a]) => expect(a).toMatch(e) );
	}));
});
