Better Bookshelves
==================
##### This is a mod that modifies Minecraft's Chiseled Bookshelves to be more useful.

[![Modrinth](https://img.shields.io/badge/dynamic/json?color=158000&label=downloads&prefix=+%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/eMBIyoJt&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/betterbookshelves)
[![CurseForge](https://cf.way2muchnoise.eu/full_693364_downloads.svg)](https://curseforge.com/minecraft/mc-mods/better-bookshelves)

## BETA
This mod is currently in beta. If you find any bugs, please report them on the [issue tracker](https://github.com/Benjamin-Norton/BetterBookshelves/issues)

### Features
* Book Type Rendering:
  * Books will render on the bookshelf differently depending on the type of book.
* Enchanting Table Integration:
  * The enchanting table will now treat Chiseled Bookshelves that contain 3 or more books as valid bookshelves for the purpose of enchanting. (The bookshelves must be facing the enchanting table)
* Book Labels:
  * Hovering over a book in the Chiseled Bookshelf will display the book's title and relevant NBT data

### Configuration
* Enable / Disable Book Labels or have the label render under the crosshair instead of on the bookshelf
  * text_preview: on/off/under_crosshair (default: on)
* Change the type of book textures used
  * per_book_texture: (true/false) (default: true)
* Change Book Label size
  * text_size: whole number between 5 and 20 (default: 10)
* Number of books required for enchanting table to recognize bookshelf
  * enchanting_table_book_requirement: whole number between 0 and 6 (default: 3, -1 to disable) 
* Change comparator output to be based on the type of book that was added to the bookshelf
  * book_type_comparator_output: (true/false) (default: false)
* Change the colour and model of each book type
  * book_textures: (array of all book types) 
    * book_type: book identifier
    * book_model: whole number between 0 and 5
    * hex_colour: hex colour code without the # (e.g. 00FF00 for green)
* Change the colour and model of each enchantment type
  * enchanted_book_textures: (array of all enchantments)
    * enchantment: enchantment identifier
    * book_model: whole number between 0 and 5 (-1 to inherit from enchantment book)
    * hex_colour: hex colour code without the # (e.g. 00FF00 for green) ("inherit" to inherit from enchantment book)

### Installation
#### Requires Fabric
1. Download the latest version of the mod from the [releases page](https://modrinth.com/mod/betterbookshelves/versions)
2. Download the latest version of [Fabric](https://fabricmc.net/use/)
3. Download the latest version of [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
4. Place the downloaded mod files in your mods folder
5. Launch Minecraft

### Reporting Bugs
If you find any bugs, please report them on the [issue tracker](https://github.com/Benjamin-Norton/BetterBookshelves/issues).