# Discord Weather Bot

[![Lint commit message](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/commit-lint.yml/badge.svg)](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/commit-lint.yml)
[![Unit tests](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/maven-test.yml/badge.svg)](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/maven-test.yml)
[![Release](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/release.yml/badge.svg)](https://github.com/yannickkirschen/discord-weather-bot/actions/workflows/release.yml)
[![GitHub release](https://img.shields.io/github/release/yannickkirschen/discord-weather-bot.svg)](https://github.com/yannickkirschen/discord-weather-bot/releases/)

This is a discord bot that receives lectures from an ICS server and weather data
from Open meteo and provides a forecast on how to pack an umbrella or not in a
Discord channel.

## Configuration

The bot is configured via Spring's `application.yml`. A template can be found
in `application-template.yml`.

This project features a scheduled workflow running the bot Tuesday through
Thursday at 6 AM for my course at university. To do so, the following variables
are set:

Name                     | Value
-------------------------|------
`SPRING_PROFILES_ACTIVE` | Spring profile to use (mostly `dhbw`).
`DHBW_ICS_UID`           | UID of the lecture plan to use. The URL can be found in `application-dhbw.yml`.
`MESSAGES_LOCALE`        | Locale to use for the messages (`DE` or `EN`, defaults to `DE`)
`DISCORD_TOKEN`          | Token of the Discord bot.
`DISCORD_CHANNEL_ID`     | ID of the channel to post messages in.

## Execution

As this is a Spring Boot application providing a runnable Fat JAR, you can just
run the JAR file after setting all environment variables:

`java -jar discord-weather-bot-<VERSION>.jar`
