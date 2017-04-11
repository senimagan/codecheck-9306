var co = require('co');
var expect = require('expect');
var runAI = require('./lib.js').runAI;

describe('Exam 2A - AI', () => {
	it('should fail without arguments', () => co(function*() {
		try {
			yield runAI();
			return Promise.reject('exited with return code 0');
		} catch (e) {}
	}));

	it('should return the answer if word list consists of only one answer word', () => co(function*() {
		const start = 'tail';
		const words = ['less'];
		const expected = 'less';
		const actual = yield runAI(start, words);
		expect(actual).toEqual(expected);
	}));

	it('should return the answer if word list contains an answer in the middle', () => co(function*() {
		const start = 'tail';
		const words = ['more', 'less', 'cat'];
		const expected = 'less';
		const actual = yield runAI(start, words);
		expect(actual).toEqual(expected);
	}));
});
