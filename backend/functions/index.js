const functions = require('firebase-functions');
const admin = require('firebase-admin');

const async = require('async');

admin.initializeApp(functions.config().firebase);

/**
 * @POST
 * Generates a map from user ids to their details such as full name and profile pic
 * urls. Accepts a list of user ids in the post request body
 */
exports.getUserDetails = functions.https.onRequest((request, response) => {
	var ref = admin.database().ref("users");
	var result = {};
	var ids = [];

	if (typeof request.body.ids === 'string') {
		ids = [request.body.ids]
	} else {
		ids = request.body.ids
	}

	async.map(ids, function(id, callback) {
		ref.orderByKey().equalTo(id).once("child_added", function(snapshot) {
			var data = snapshot.val();
			result[id] = {
				"fullName": data.fullName,
				"photoUrl": data.photoUrl
			}

			return callback(null, result[id]);
		});

	}, function(err, contents) {
		if (err) console.error(err);
		console.log(contents);
		response.send(result);
	});
});

/**
 * Updates a conversation's most recent message whenever a message is added
 * to the conversation
 *
 * Note: This method would need to be modified if message delete feature is
 * implemented.
 */
exports.updateMostRecentMessage = functions.database.ref('/messages/{messageId}')
	.onWrite(event => {
		var ref = admin.database().ref("conversations");
		const message = event.data.val();

		return ref.child(message.conversation).child("lastMessage").set(message);
	});

/**
 * Sets a unique username for the newly created user
 */
exports.setUniqueUsername = functions.auth.user().onCreate(event => {
	var ref = admin.database().ref("users");

	const id = event.data.uid;
	const email = event.data.email;
	// string before '@' symbol with trailing digits removed
	const potentialUsername = email.substring(0, email.indexOf('@')).replace(/\d+$/, '');

	ref.once("value", function(snapshot) {
		var matchedUsernames = 0;
		snapshot.forEach(function(data) {
			const user = data.val();
			if (user.username !== undefined && user.username.startsWith(potentialUsername)) {
				matchedUsernames += 1;
			}
		});

		const uniqueUsername = potentialUsername + (matchedUsernames ? matchedUsernames.toString() : "");
		console.log(uniqueUsername);
		return ref.child(id).child("username").set(uniqueUsername);
	});
});

exports.sendNotificationOnNewMessage = functions.database.ref("/messages/{messageId}")
	.onWrite(event => {
		const snapshot = event.data;
		// only send a notification when a new message has been created
		if (snapshot.previous.val()) {
			return;
		}

		const message = snapshot.val();
		var ref = admin.database().ref("conversations");

		ref.child(message.conversation).child("participants").once("value", function(snapshot) {
			var participants = snapshot.val();
			// do not send notification to self
			const authorIdx = participants.indexOf(message.author);
			participants.splice(authorIdx, 1);

			var promises = [];
			// creates promises to retrieve tokens of users to send notification to
			for (var i = 0; i < participants.length; ++i) {
				const promise = admin.database().ref(`/users/${participants[i]}/instanceId`).once('value');
				promises.push(promise);
			}

			admin.database().ref("users").child(message.author).child("fullName")
				.once("value", function(snapshot) {
					const payload = {
						notification: {
							title: `${snapshot.val()} sent a message`,
							body: message.content
						}
					}

					return Promise.all(promises).then(results => {
						var tokens = [];
						for (var i = 0; i < results.length; ++i) {
							const token = results[i].val();
							if (token.length > 0) {
								tokens.push(token);
							}
						}

						// pushes notification to device(s)
						admin.messaging().sendToDevice(tokens, payload);
					});
				});
		});
	});