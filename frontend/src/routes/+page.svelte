<!-- Copyright 2023 DDS Permissions Manager Authors-->
<script>
	import { isAuthenticated } from '../stores/authentication';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { httpAdapter } from '../appconfig';
	import wavesSVG from '../icons/waves.svg';
	import googleSVG from '../icons/google.svg';
	import groups from '../stores/groups';
	import groupsTotalPages from '../stores/groupsTotalPages';
	import groupsTotalSize from '../stores/groupsTotalSize';
	import messages from '$lib/messages.json';

	const URL_PREFIX = import.meta.env.VITE_BACKEND_URL;
	const itemsPerPage = 10;

	export let data, errors;

	const preloadGroups = async (page = 0) => {
		const res = await httpAdapter.get(`/groups?page=${page}&size=${itemsPerPage}`);

		if (res.data) {
			groupsTotalPages.set(res.data.totalPages);
			groupsTotalSize.set(res.data.totalSize);
		}

		groups.set(res.data.content);
		groupsTotalPages.set(res.data.totalPages);
	};

	onMount(async () => {
		if ($isAuthenticated) {
			await preloadGroups();
			goto('/groups', true);
		}
	});
</script>

<svelte:head>
	<title>{messages['login']['title']}</title>
	<meta name="description" content="Permissions Manager" />
</svelte:head>

{#if $isAuthenticated === false}
	<center>
		<h1>{messages['login']['header']}</h1>
	</center>
	<br />
	<div class="container-box">
		<img
			src={wavesSVG}
			alt="Login"
			class="icon"
			style="margin: 1rem 0 1rem 1.5rem; filter: contrast(60%);"
		/>
		<span style="margin-left: 1rem; vertical-align: middle; filter: contrast(60%);"
			>{messages['login']['label']}</span
		>
		<hr />
		<center>
			<!-- svelte-ignore a11y-click-events-have-key-events -->
			<div class="login-button" on:click={() => goto(`${URL_PREFIX}/oauth/login/google`, true)}>
				<img
					src={googleSVG}
					alt="Login"
					class="icon"
					style="scale: 55%; margin: 0.1rem 0 0.1rem 0;"
				/>
				<span style="margin-left: 0; font-size: 1.4rem"
					><a rel="external" href={`${URL_PREFIX}/oauth/login/google`}
						>{messages['login']['button']}</a
					></span
				>
			</div>
		</center>
	</div>
{/if}

<style>
	span {
		font-weight: 500;
		margin-left: 0.5rem;
		font-size: 1.5rem;
		vertical-align: middle;
	}

	h1 {
		font-weight: 450;
		font-stretch: 110%;
		color: black;
		padding-left: 2rem;
	}

	hr {
		margin: 1.5rem 1.3rem 1.8rem 1.3rem;
		border-color: rgba(0, 0, 0, 0.15);
		border-style: solid;
		border-width: 0.05rem;
	}

	.container-box {
		display: flexbox;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: center;
		align-items: center;
		align-content: stretch;
		border-radius: 18px;
		border-width: 1px;
		border-style: solid;
		border-color: #444444;
		margin: auto;
		width: 25.5rem;
		scale: 70%;
	}

	.login-button {
		scale: 90%;
		cursor: pointer;
		vertical-align: middle;
		background-color: rgba(0, 0, 0, 0);
		border-color: rgb(237, 237, 237);
		border-radius: 50px;
		border-width: 1px;
		border-style: solid;
		margin-bottom: 1rem;
		text-align: center;
		box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.4);
	}

	.login-button a {
		color: #6751a4;
	}

	a:hover {
		text-decoration: none;
	}
</style>
