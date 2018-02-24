
use BlogServer
db.Posts.drop()
db.Users.drop()
db.createCollection("Posts")
db.Posts.insert({
	postid: 1,
	username: "cs144",
	created: 1518669344517,
	modified: 1518669344517,
	title: "Title 1",
	body: "Hello, world!"
})
db.Posts.insert({
	postid: 2,
	username: "cs144",
	created: 1518669658420,
	modified: 1518669658420,
	title: "Title 2",
	body: "I am here."
})
db.Posts.insert({
	postid: 3,
	username: "cs144",
	created: 1518669658425,
	modified: 1518669658429,
	title: "Title 3",
	body: "I am here 3."
})
db.Posts.insert({
	postid: 4,
	username: "cs144",
	created: 1518669658420,
	modified: 1518669658420,
	title: "Title 4",
	body: "I am here 4."
})
db.Posts.insert({
	postid: 5,
	username: "cs144",
	created: 1518669658420,
	modified: 1518669658420,
	title: "Title 5",
	body: "I am here 5."
})
db.Posts.insert({
	postid: 6,
	username: "cs144",
	created: 1518669658420,
	modified: 1518669658420,
	title: "Title 6",
	body: "I am here 6."
})

db.createCollection("Users")
db.Users.insert({
	username: "cs144",
	password: "$2a$10$2DGJ96C77f/WwIwClPwSNuQRqjoSnDFj9GDKjg6X/PePgFdXoE4W6"
})
db.Users.insert({
	username: "user2",
	password: "$2a$10$kTaFlLbfY1nnHnjb3ZUP3OhfsfzduLwl2k/gKLXvHew9uX.1blwne"
})


exit